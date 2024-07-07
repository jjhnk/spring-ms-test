package hy.api.core.estate.lease;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.UnitSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The LeaseService interface defines the REST API for managing leases.
 *
 * @see Lease
 */
@RequestMapping("/property/lease")
@SecurityRequirement(name = "security_auth")
@Tag(name = "Lease", description = "The Lease API")
public interface LeaseController {

  /**
   * Creates a new lease.
   *
   * @param lease the lease
   * @return the created lease
   */
  @Operation(summary = "${api.lease.main.create.description}", description = "${api.lease.main.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(consumes = "application/json", produces = "application/json")
  public Lease createLease(Lease lease);

  /**
   * Gets a lease.
   *
   * @param headers the HTTP headers
   * @param id the ID of the lease
   * @return the lease
   */
  @Operation(summary = "${api.lease.main.get.description}", description = "${api.lease.main.get.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{id}", produces = "application/json")
  public Lease getLease(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Gets all leases.
   *
   * @param headers the HTTP headers
   * @return all leases
   *
   * @see Tenant
   * @see UnitSummary
   */
  @Operation(summary = "${api.lease.main.getAll.description}", description = "${api.lease.main.getAll.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/all", produces = "application/json")
  public List<Lease> getLeases(
    @RequestHeader HttpHeaders headers,
    @RequestParam(value = "tenantId", required = false, defaultValue = "0") int tenantId,
    @RequestParam(value = "unitId", required = false, defaultValue = "0") int unitId);

  /**
   * Updates a lease.
   *
   * @param headers the HTTP headers
   * @param id the ID of the lease
   * @param lease the updated lease
   * @return the updated lease
   */
  @Operation(summary = "${api.lease.main.update.description}", description = "${api.lease.main.update.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public Lease updateLease(@RequestHeader HttpHeaders headers, @PathVariable("id") int id, Lease lease);

  /**
   * Deletes a lease.
   *
   * @param headers the HTTP headers
   * @param id the ID of the lease
   */
  public void deleteLease(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);
}
