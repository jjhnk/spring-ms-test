package hy.api.core.product;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Mono;

/**
 * The ProductService interface defines the REST API for managing products.
 *
 * @see Product
 */
@RequestMapping("/product")
public interface ProductService {

  /**
   * Create a new product.
   *
   * @param body the product to create
   * @return the created product
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  Mono<Product> createProduct(@RequestBody Product body);

  /**
   * Retrieves a product by its ID.
   *
   * @param productId the ID of the product to retrieve
   * @return the product with the specified ID, if found, else null
   */
  @GetMapping(value = "/{productId}", produces = "application/json")
  Mono<Product> getProduct(@PathVariable("productId") int productId,
    @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
    @RequestParam(value = "faultPercent", required = false, defaultValue = "0") int faultPercent);

  /**
   * Delete a product by its ID.
   *
   * @param productId the ID of the product to delete
   */
  @DeleteMapping(value = "/{productId}")
  Mono<Void> deleteProduct(@PathVariable("productId") int productId);
}
