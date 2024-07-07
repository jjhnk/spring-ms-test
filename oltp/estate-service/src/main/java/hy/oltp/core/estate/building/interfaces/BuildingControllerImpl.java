package hy.oltp.core.estate.building.interfaces;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.buildings.BuildingController;
import hy.oltp.core.estate.building.service.BuildingService;

@RestController
public class BuildingControllerImpl implements BuildingController {
  private final BuildingService service;

  public BuildingControllerImpl(BuildingService service) {
    this.service = service;
  }

  @Override
  public Building createBuilding(@RequestBody Building building) {
    return service.createBuilding(building);
  }

  @Override
  public Building getBuilding(@RequestHeader HttpHeaders headers, @PathVariable int id) {
    return service.getBuilding(headers, id);
  }

  @Override
  public List<Building> getBuildings(@RequestHeader HttpHeaders headers) {
    return service.getBuildings(headers);
  }

  @Override
  public Building updateBuilding(
    @RequestHeader HttpHeaders headers,
    @PathVariable int id,
    @RequestBody Building building) {
    return service.updateBuilding(headers, id, building);
  }

  @Override
  public void deleteBuilding(@RequestHeader HttpHeaders headers, @PathVariable int id) {
    service.deleteBuilding(headers, id);
  }
}
