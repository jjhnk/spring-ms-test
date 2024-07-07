package hy.api.core.estate.buildings;

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

@RequestMapping("/building")
@SecurityRequirement(name = "security_auth")
@Tag(name = "Building", description = "Building API")
public interface BuildingController {

  /**
   * Creates a new building
   *
   * @param building
   * @return the created building
   */
  @Operation(summary = "${api.building.main.create.description}", description = "${api.building.main.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(consumes = "application/json", produces = "application/json")
  public Building createBuilding(Building building);

  /**
   * Gets a building
   *
   * @param headers the HTTP headers
   * @param id the ID of the building
   * @return the building
   */
  @Operation(summary = "${api.building.main.get.description}", description = "${api.building.main.get.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{id}", produces = "application/json")
  public Building getBuilding(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Gets a list of building
   *
   * @param headers the HTTP headers
   * @return the list of building
   */
  @Operation(summary = "${api.building.main.get-list.description}", description = "${api.building.main.get-list.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/all", produces = "application/json")
  public List<Building> getBuildings(@RequestHeader HttpHeaders headers);

  /**
   * Updates a building
   *
   * @param headers the HTTP headers
   * @param id the ID of the building
   * @param building the updated building
   * @return the updated building
   */
  @Operation(summary = "${api.building.main.update.description}", description = "${api.building.main.update.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public Building updateBuilding(@RequestHeader HttpHeaders headers, @PathVariable("id") int id, Building building);

  /**
   * Deletes a building
   *
   * @param headers the HTTP headers
   * @param id the ID of the building
   */
  @Operation(summary = "${api.building.main.delete.description}", description = "${api.building.main.delete.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "204", description = "${api.responseCodes.noContent.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @DeleteMapping(value = "/{id}")
  public void deleteBuilding(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

}
