package hy.microservices.composite.product.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  private static final String SCOPE_PRODUCT_READ = "SCOPE_product:read";
  private static final String SCOPE_PRODUCT_WRITE = "SCOPE_product:write";
  private static final String MICRO_SERVICE_URL = "/product-composite/**";

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable)
      .authorizeExchange(exchange ->
        exchange
          .pathMatchers("/product-composite/openapi/**").permitAll()
          .pathMatchers("/actuator/**").permitAll()
          .pathMatchers(POST, MICRO_SERVICE_URL).hasAuthority(SCOPE_PRODUCT_WRITE)
          .pathMatchers(GET, MICRO_SERVICE_URL).hasAuthority(SCOPE_PRODUCT_READ)
          .pathMatchers(DELETE, MICRO_SERVICE_URL).hasAuthority(SCOPE_PRODUCT_WRITE)
          .anyExchange().authenticated()
      )
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
    return http.build();
  }

  // @Bean
  // ReactiveJwtDecoder jwtDecoder() {
  //     return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
  // }
}