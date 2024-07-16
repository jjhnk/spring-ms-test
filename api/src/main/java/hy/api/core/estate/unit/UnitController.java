package hy.api.core.estate.unit;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.tenant.Tenant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The UnitService interface defines the REST API for managing units.
 *
 * @see Unit
 */
@RequestMapping("/estate/unit")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Unit", description = "Unit API")
public interface UnitController {
  /**
   * Creates a new unit.
   *
   * @param unit the unit to create
   * @return the created unit
   */
  @Operation(
    summary = "${api.estate.unit.main.create.description}",
    description = "${api.estate.unit.main.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(consumes = "application/json", produces = "application/json")
  public Unit createUnit(@RequestBody Unit unit);

  /**
   * Retrieves a unit by its ID.
   *
   * @param headers the HTTP headers
   * @param id the ID of the unit
   * @return the unit with the specified ID
   */
  @Operation(summary = "${api.estate.unit.main.get.description}", description = "${api.estate.unit.main.get.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{id}", produces = "application/json")
  public Unit getUnit(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Retrieves all units.
   *
   * @param headers the HTTP headers
   * @param buildingId the ID of the property
   * @param tenantId the ID of the tenant
   * @param showDetails whether to show details
   * @return the list of units
   *
   * @see Building
   * @see Tenant
   */
  @Operation(
    summary = "${api.estate.unit.main.get-list.description}",
    description = "${api.estate.unit.main.get-list.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/all", produces = "application/json")
  public List<Unit> getUnits(
    @RequestHeader HttpHeaders headers,
    @RequestParam(value = "buildingId", required = false, defaultValue = "0") int buildingId,
    @RequestParam(value = "tenantId", required = false, defaultValue = "0") int tenantId,
    @RequestParam(value = "showDetails", required = false, defaultValue = "false") boolean showDetails);

  /**
   * Updates an existing unit.
   *
   * @param headers the HTTP headers
   * @param id the ID of the unit
   * @param unit the updated unit
   * @return the updated unit
   */
  @Operation(
    summary = "${api.estate.unit.main.update.description}",
    description = "${api.estate.unit.main.update.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public Unit updateUnit(@RequestHeader HttpHeaders headers, @PathVariable("id") int id, @RequestBody Unit unit);

  /**
   * Deletes a unit by its ID.
   *
   * @param headers the HTTP headers
   * @param id the ID of the unit
   */
  @Operation(
    summary = "${api.estate.unit.main.delete.description}",
    description = "${api.estate.unit.main.delete.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "204", description = "${api.responseCodes.noContent.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @DeleteMapping(value = "/{id}")
  public void deleteUnit(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Creates a new unit details
   *
   * @param id the ID of the unit
   * @param unitDetail the unit details
   * @return the created unit
   */
  @Operation(
    summary = "${api.estate.unit.detail.create.description}",
    description = "${api.estate.unit.detail.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(value = "/{id}/details", consumes = "application/json", produces = "application/json")
  public UnitDetail createUnitDetail(@PathVariable("id") int id, @RequestBody UnitDetail unitDetail);

  /**
   * Retrieves a unit details
   *
   * @param headers the HTTP headers
   * @param id the ID of the unit
   * @return the unit
   */
  @Operation(summary = "${api.estate.unit.detail.get.description}", description = "${api.estate.unit.detail.get.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{id}/details", produces = "application/json")
  public UnitDetail getUnitDetail(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Updates an existing unit details
   *
   * @param headers the HTTP headers
   * @param id the ID of the unit
   * @param unitDetails the updated unit details
   * @return the updated unit
   */
  @Operation(
    summary = "${api.estate.unit.detail.update.description}",
    description = "${api.estate.unit.detail.update.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PutMapping(value = "/{id}/details", consumes = "application/json", produces = "application/json")
  public UnitDetail updateUnitDetail(
    @RequestHeader HttpHeaders headers,
    @PathVariable("id") int id,
    @RequestBody UnitDetail unitDetails);

  /**
   * Deletes a unit details
   *
   * @param headers the HTTP headers
   * @param id the ID of the unit
   */
  @Operation(
    summary = "${api.estate.unit.detail.delete.description}",
    description = "${api.estate.unit.detail.delete.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "204", description = "${api.responseCodes.noContent.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @DeleteMapping(value = "/{id}/details")
  public void deleteUnitDetail(@RequestHeader HttpHeaders headers, @PathVariable("id") int id);

  /**
   * Creates a new room
   *
   * @param unitId the ID of the unit
   * @param room the room
   * @return the created room
   */
  @Operation(
    summary = "${api.estate.unit.room.create.description}",
    description = "${api.estate.unit.room.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(value = "/{unitId}/details/room", consumes = "application/json", produces = "application/json")
  public Room createRoom(@PathVariable("unitId") int unitId, @RequestBody Room room);

  /**
   * Creates a list of rooms
   *
   * @param unitId the ID of the unit
   * @param rooms the rooms
   * @return the created rooms
   */
  @Operation(
    summary = "${api.estate.unit.room-list.create.description}",
    description = "${api.estate.unit.room-list.create.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PostMapping(value = "/{unitId}/details/rooms", consumes = "application/json", produces = "application/json")
  public List<Room> createRooms(@PathVariable("unitId") int unitId, @RequestBody List<Room> rooms);

  /**
   * Retrieves a room
   *
   * @param headers the HTTP headers
   * @param unitId the ID of the unit
   * @param roomId the ID of the room
   * @return the room
   */
  @Operation(summary = "${api.estate.unit.room.get.description}", description = "${api.estate.unit.room.get.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{unitId}/details/room/{roomId}", produces = "application/json")
  public Room getRoom(
    @RequestHeader HttpHeaders headers,
    @PathVariable("unitId") int id,
    @PathVariable("roomId") int roomId);

  /**
   * Retrieves a list of rooms
   *
   * @param headers the HTTP headers
   * @param unitId the ID of the unit
   * @return the rooms
   */
  @Operation(
    summary = "${api.estate.unit.room.get-list.description}",
    description = "${api.estate.unit.room.get-list.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @GetMapping(value = "/{unitId}/details/room/all", produces = "application/json")
  public List<Room> getRooms(@RequestHeader HttpHeaders headers, @PathVariable("unitId") int unitId);

  /**
   * Updates an existing room
   *
   * @param headers the HTTP headers
   * @param unitId the ID of the unit
   * @param roomId the ID of the room
   * @param room the updated room
   * @return the updated room
   */
  @Operation(
    summary = "${api.estate.unit.room.update.description}",
    description = "${api.estate.unit.room.update.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @PutMapping(value = "/{unitId}/details/room/{roomId}", consumes = "application/json", produces = "application/json")
  public Room updateRoom(
    @RequestHeader HttpHeaders headers,
    @PathVariable("unitId") int unitId,
    @PathVariable("roomId") int roomId,
    @RequestBody Room room);

  /**
   * Deletes a room
   *
   * @param headers the HTTP headers
   * @param unitId the ID of the unit
   * @param roomId the ID of the room
   */
  @Operation(
    summary = "${api.estate.unit.room.delete.description}",
    description = "${api.estate.unit.room.delete.notes}")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
      @ApiResponse(responseCode = "204", description = "${api.responseCodes.noContent.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")})
  @DeleteMapping(value = "/{unitId}/details/room/{roomId}")
  public void deleteRoom(
    @RequestHeader HttpHeaders headers,
    @PathVariable("unitId") int unitId,
    @PathVariable("roomId") int roomId);
}
