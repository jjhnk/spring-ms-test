package hy.microservices.core.product.services;

import org.springframework.web.bind.annotation.RestController;
import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
  private final ServiceUtil serviceUtil;

  @Override
  public Product getProduct(int productId) {
    log.debug("request for productId: {}", productId);

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 13) {
      throw new NotFoundException("No product found for productId: " + productId);
    }

    return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
  }
}
