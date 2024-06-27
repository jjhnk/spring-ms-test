package hy.microservices.composite.product.services;

import static java.util.logging.Level.FINE;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Supplier;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import hy.api.composite.product.ProductAggregate;
import hy.api.composite.product.ProductCompositeService;
import hy.api.composite.product.RecommendationSummary;
import hy.api.composite.product.ReviewSummary;
import hy.api.composite.product.ServiceAddresses;
import hy.api.core.product.Product;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.review.Review;
import hy.microservices.composite.product.services.tracing.ObservationUtil;
import hy.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductCompositeServiceImpl implements ProductCompositeService {
  private static final String CLASS_NAME = ProductCompositeServiceImpl.class.getSimpleName();

  private final ServiceUtil serviceUtil;
  private final ObservationUtil observationUtil;
  private final ProductCompositeIntegration integration;
  private final SecurityContext nullSecCtx = new SecurityContextImpl();

  // @formatter:off
  @Override
  public Mono<Void> createProduct(ProductAggregate body) {
    return observationWithProductInfo(body.getProductId(), () -> createProductInternal(body));
  }

  Mono<Void> createProductInternal(ProductAggregate body) {
    try {
      log.info("{}::createProduct() createCompositeProduct: {}", getClass().getSimpleName(), body);
      List<Mono<?>> monoList = new ArrayList<>();
      monoList.add(getLogAuthorizationInfoMono());

      Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
      monoList.add(integration.createProduct(product));

      if (body.getRecommendations() != null) {
        body.getRecommendations()
          .forEach(r -> {
            Recommendation recommendation = new Recommendation(body.getProductId(), r.getRecommendationId(),
              r.getAuthor(), r.getRate(), r.getContent(), null);
            monoList.add(integration.createRecommendation(recommendation));
          });
      }

      if (body.getReviews() != null) {
        body.getReviews()
          .forEach(r -> {
            Review review =
              new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
            monoList.add(integration.createReview(review));
          });
      }

      return Mono.zip(r -> "", monoList.toArray(new Mono[0]))
        .doOnError(ex -> log.warn("{}::createProduct() failed: {}", CLASS_NAME, ex.toString()))
        .then();

    } catch (RuntimeException re) {
      log.warn("Failed to create aggregate", re);
      throw new RuntimeException(re);
    }
  }

  @Override
  public Mono<ProductAggregate> getProduct(HttpHeaders requestHeaders, int productId, int delay, int faultPercent) {
    return observationWithProductInfo(productId, () -> getProductInternal(requestHeaders, productId, delay, faultPercent));
  }

  Mono<ProductAggregate> getProductInternal(HttpHeaders requestHeaders, int productId, int delay, int faultPercent) {
    log.info("ProductCompositeServiceImpl::getProduct - productId: {}", productId);

    HttpHeaders headers = getHeaders(requestHeaders, "X-group");

    return Mono
      .zip(
        getSecurityContextMono(),
        integration.getProduct(headers, productId, delay, faultPercent),
        integration.getRecommendations(headers, productId).collectList(),
        integration.getReviews(headers, productId).collectList())
      .map(this::createProductAggregate)
      .doOnError(ex -> log.warn("getCompositeProduct failed: {}", ex.toString()))
      .log(log.getName(), FINE);
  }

  private HttpHeaders getHeaders(HttpHeaders requestHeaders, String... headers) {
    log.trace("Will look for {} headers: {}", headers.length, headers);
    HttpHeaders h = new HttpHeaders();
    for (String header : headers) {
      List<String> value = requestHeaders.get(header);
      if (value != null) {
        h.addAll(header, value);
      }
    }
    log.trace("Will transfer {}, headers: {}", h.size(), h);
    return h;
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    return observationWithProductInfo(productId, () -> deleteProductInternal(productId));
  }

  private Mono<Void> deleteProductInternal(int productId) {
    try {
      log.info("{}::deleteProduct() productId: {}", CLASS_NAME, productId);

      return Mono
        .zip(
          r -> "",
          getLogAuthorizationInfoMono(),
          integration.deleteProduct(productId),
          integration.deleteRecommendations(productId),
          integration.deleteReviews(productId))
        .doOnError(ex -> log.warn("{}::deleteProduct() failed: {}", CLASS_NAME, ex.toString()))
        .log(log.getName(), FINE)
        .then();
    } catch (RuntimeException re) {
      log.warn("{}::deleteProduct() failed: {}", CLASS_NAME, re);
      throw new RuntimeException(re);
    }
  }

  <T> T observationWithProductInfo(int productId, Supplier<T> supplier) {
    return observationUtil.observe(
      "composite observation",
      "product info",
      "productId",
      String.valueOf(productId),
      supplier);
  }

  private ProductAggregate createProductAggregate(
    Tuple4<SecurityContext, Product, List<Recommendation>, List<Review>> values) {
    return createProductAggregate(
      values.getT1(),
      values.getT2(),
      values.getT3(),
      values.getT4(),
      serviceUtil.getServiceAddress());
  }
  // @formatter:on

  // @formatter:off
  private ProductAggregate createProductAggregate(
    SecurityContext sc,
    Product product,
    List<Recommendation> recommendations,
    List<Review> reviews,
    String serviceAddress) {

    logAuthorizationInfo(sc);

    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    List<RecommendationSummary> recommendationSummaries = (recommendations == null)
      ? null
      : recommendations.stream()
        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
        .collect(Collectors.toList());

    List<ReviewSummary> reviewSummaries = (reviews == null)
      ? null
      : reviews.stream()
        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
        .collect(Collectors.toList());

    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && !reviews.isEmpty())
      ? reviews.get(0)
        .getServiceAddress()
      : "";
    String recommendationAddress = (recommendations != null && !recommendations.isEmpty())
      ? recommendations.get(0)
        .getServiceAddress()
      : "";

    ServiceAddresses serviceAddresses =
      new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
  // @formatter:on

  private Mono<SecurityContext> getLogAuthorizationInfoMono() {
    return getSecurityContextMono().doOnNext(this::logAuthorizationInfo);
  }

  private Mono<SecurityContext> getSecurityContextMono() {
    return ReactiveSecurityContextHolder.getContext()
      .defaultIfEmpty(nullSecCtx);
  }

  private void logAuthorizationInfo(SecurityContext sc) {
    if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
      Jwt jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
      logAuthorizationInfo(jwtToken);
    } else {
      log.warn("No JWT based Authentication supplied, running tests are we?");
    }
  }

  // @formatter:off
  private void logAuthorizationInfo(Jwt jwt) {
    if (jwt == null) {
      log.warn("No JWT supplied, running tests are we?");
    } else {
      if (log.isDebugEnabled()) {
        URL issuer = jwt.getIssuer();
        List<String> audience = jwt.getAudience();
        Object subject = jwt.getClaims().get("sub");
        Object scopes = jwt.getClaims().get("scope");
        Object expires = jwt.getClaims().get("exp");

        log.debug("Authorization info: Subject: {}, scopes: {}, expires {}: issuer: {}, audience: {}",
          subject,
          scopes,
          expires,
          issuer,
          audience);
      }
    }
  }
  // @formatter:on
}
