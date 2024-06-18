package hy.microservices.core.product.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.microservices.core.product.persistence.ProductEntity;
import hy.microservices.core.product.persistence.ProductRepository;
import hy.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
  private final ServiceUtil serviceUtil;
  private final ProductRepository repository;
  private final ProductMapper mapper;

  @Override
  public Product createProduct(Product body) {
    try {
      ProductEntity entity = mapper.apiToEntity(body);
      entity = repository.save(entity);

      log.debug("ProductServiceImpl::createProduct - entity: {}", entity);
      return mapper.entityToApi(entity);
    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
    }
  }

  @Override
  public Product getProduct(int productId) {
    if (productId < 1)
      throw new InvalidInputException("Invalid productId: " + productId);

    ProductEntity entity = repository.findByProductId(productId)
      .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

    Product response = mapper.entityToApi(entity);
    response.setServiceAddress(serviceUtil.getServiceAddress());

    log.debug("ProductServiceImpl::getProduct - product: {}",  response);
    return response;
  }

  @Override
  public void deleteProduct(int productId) {
    log.debug("ProductServiceImpl::deleteProduct - productId: {}", productId);
    repository.findByProductId(productId).ifPresent(repository::delete);
  }
}
