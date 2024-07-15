package hy.oltp.core.estate.unit.interfaces;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitController;
import hy.api.core.estate.unit.UnitDetail;
import hy.oltp.core.estate.unit.service.UnitService;

@RestController
public class UnitControllerImpl implements UnitController {
  private final UnitService unitService;

  public UnitControllerImpl(UnitService unitService) {
    this.unitService = unitService;
  }

  @Override
  public Unit createUnit(@RequestBody Unit unit) {
    return unitService.createUnit(unit);
  }

  @Override
  public Unit getUnit(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
    return unitService.getUnit(headers, id);
  }

  @Override
  public List<Unit> getUnits(
    @RequestHeader HttpHeaders headers,
    @RequestParam(value = "buildingId", required = false, defaultValue = "0") int buildingId,
    @RequestParam(value = "tenantId", required = false, defaultValue = "0") int tenantId,
    @RequestParam(value = "showDetails", required = false, defaultValue = "false") boolean showDetails) {
    return unitService.getUnits(headers, buildingId, tenantId);
  }

  @Override
  public Unit updateUnit(@RequestHeader HttpHeaders headers, @PathVariable("id") int id, @RequestBody Unit unit) {
    return unitService.updateUnit(headers, id, unit);
  }

  @Override
  public void deleteUnit(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
    unitService.deleteUnit(headers, id);
  }

  @Override
  public UnitDetail createUnitDetail(@PathVariable("id") int id, @RequestBody UnitDetail unitDetail) {
    return unitService.createUnitDetail(id, unitDetail);
  }

  @Override
  public UnitDetail getUnitDetail(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
    return unitService.getUnitDetail(headers, id);
  }

  @Override
  public UnitDetail updateUnitDetail(
    @RequestHeader HttpHeaders headers,
    @PathVariable("id") int id,
    @RequestBody UnitDetail unitDetail) {
    return unitService.updateUnitDetail(headers, id, unitDetail);
  }

  @Override
  public void deleteUnitDetail(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
    unitService.deleteUnitDetail(headers, id);
  }

  @Override
  public Room createRoom(@PathVariable int unitId, @RequestBody Room room) {
    return unitService.createRoom(unitId, room);
  }

  @Override
  public List<Room> createRooms(@PathVariable int unitId, @RequestBody List<Room> rooms) {
    return unitService.createRooms(unitId, rooms);
  }

  @Override
  public Room getRoom(
    @RequestHeader HttpHeaders headers,
    @PathVariable("unitId") int id,
    @PathVariable("roomId") int roomId) {
    return unitService.getRoom(headers, id, roomId);
  }

  @Override
  public List<Room> getRooms(@RequestHeader HttpHeaders headers, @PathVariable("unitId") int unitId) {
    return unitService.getRooms(headers, unitId);
  }

  @Override
  public Room updateRoom(
    @RequestHeader HttpHeaders headers,
    @PathVariable("unitId") int unitId,
    @PathVariable("roomId") int roomId,
    @RequestBody Room room) {
    return unitService.updateRoom(headers, unitId, roomId, room);
  }

  @Override
  public void deleteRoom(
    @RequestHeader HttpHeaders headers,
    @PathVariable("unitId") int unitId,
    @PathVariable("roomId") int roomId) {
    unitService.deleteRoom(headers, unitId, roomId);
  }

}
