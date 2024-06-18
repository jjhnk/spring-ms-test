package hy.api.composite.product;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/product-composite")
@Tag(name = "ProductComposite", description = "REST API for composite product information")
public interface ProductCompositeService {

  /**
   * Create a new composite product.
   *
   * @param body the product to create
   */
  @Operation(summary = "${api.product-composite.create-composite-product.description}",
      description = "${api.product-composite.create-composite-product.notes}")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
          @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(consumes = "application/json")
  void createProduct(@RequestBody ProductAggregate body);

  /**
   * Retrieve a composite product by ID. The composite product includes the product itself and the reviews for that
   * product.
   *
   * @param productId the ID of the product
   * @return the composite product
   */
  @Operation(summary = "${api.product-composite.get-composite-product.description}",
      description = "${api.product-composite.get-composite-product.notes}")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
    @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
    @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
    @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{productId}", produces = "application/json")
  ProductAggregate getProduct(@PathVariable int productId);

  /**
   * Delete a product by its ID.
   * @param productId the ID of the product
   */
  @Operation(summary = "${api.product-composite.delete-composite-product.description}",
      description = "${api.product-composite.delete-composite-product.notes}")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
    @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @DeleteMapping(value = "/{productId}")
  void deleteProduct(@PathVariable int productId);
}
