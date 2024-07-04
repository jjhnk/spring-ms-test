package hy.api.core.property.tenant;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The TenantService interface defines the REST API for managing tenants.
 *
 * @see Tenant
 */
@RequestMapping("/property/tenant")
@SecurityRequirement(name = "security_auth")
@Tag(name = "Tenant", description = "Tenant API")
public interface TenantService {

  /**
   * Creates a new tenant
   *
   * @param tenant the tenant
   * @return the created tenant
   */
  @Operation(
    summary = "${api.property.tenant.main.create.description}",
    description = "${api.property.tenant.main.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(consumes = "application/json", produces = "application/json")
  public Tenant createTenant(Tenant tenant);

  /**
   * Gets a tenant
   *
   * @param headers the HTTP headers
   * @param id the ID of the tenant
   * @return the tenant
   */
  @Operation(
    summary = "${api.property.tenant.main.get.description}",
    description = "${api.property.tenant.main.get.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{id}", produces = "application/json")
  public Tenant getTenant(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Gets a list of tenants
   *
   * @param headers the HTTP headers
   * @return the list of tenants
   */
  @Operation(
    summary = "${api.property.tenant.main.get-list.description}",
    description = "${api.property.tenant.main.get-list.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/all", produces = "application/json")
  public List<Tenant> getTenants(@RequestHeader HttpHeaders headers);

  /**
   * Updates a tenant
   *
   * @param headers the HTTP headers
   * @param id the ID of the tenant
   * @param tenant the updated tenant
   * @return the updated tenant
   */
  @Operation(
    summary = "${api.property.tenant.main.update.description}",
    description = "${api.property.tenant.main.update.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public Tenant updateTenant(@RequestHeader HttpHeaders headers, @PathVariable("id") int id, Tenant tenant);

  /**
   * Deletes a tenant
   *
   * @param headers the HTTP headers
   * @param id the ID of the tenant
   */
  @Operation(
    summary = "${api.property.tenant.main.delete.description}",
    description = "${api.property.tenant.main.delete.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "204", description = "${api.responseCodes.noContent.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @DeleteMapping(value = "/{id}")
  public void deleteTenant(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

}
