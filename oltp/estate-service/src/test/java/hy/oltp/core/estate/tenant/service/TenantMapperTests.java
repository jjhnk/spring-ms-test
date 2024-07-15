package hy.oltp.core.estate.tenant.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.tenant.Tenant;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;

class TenantMapperTests {
  private final TenantMapper mapper = TenantMapper.INSTANCE;

  @Test
  void mapperTest() {
    Set<Lease> leases =
      Set.of(new Lease(1, null, null, null), new Lease(2, null, null, null), new Lease(3, null, null, null));

    Tenant api = new Tenant(1,"firstName", "lastName", "email@email.com", "01012345678");
    api.setLeases(leases);
    TenantEntity entity = mapper.apiToEntity(api);

    assertThat(api.getId()).isEqualTo(entity.getId());
    assertThat(api.getFirstName()).isEqualTo(entity.getFirstName());
    assertThat(api.getLastName()).isEqualTo(entity.getLastName());
    assertThat(api.getEmail()).isEqualTo(entity.getEmail());
    assertThat(api.getPhoneNumber()).isEqualTo(entity.getPhoneNumber());

    // @formatter:off
    var apiList = api.getLeases().stream().collect(Collectors.toList());
    apiList.sort((x, y) -> x.getId() - y.getId());
    var entityList = entity.getLeases().stream().collect(Collectors.toList());
    entityList.sort((x, y) -> x.getId() - y.getId());
    IntStream.range(0, apiList.size())
      .mapToObj(i -> Map.entry(apiList.get(i), entityList.get(i)))
      .collect(Collectors.toList())
      .forEach(x -> assertThat(x.getKey().getId()).isEqualTo(x.getValue().getId()));
    // @formatter:on

    Tenant api2 = mapper.entityToApi(entity);
    assertThat(api.getId()).isEqualTo(api2.getId());
    assertThat(api.getFirstName()).isEqualTo(api2.getFirstName());
    assertThat(api.getLastName()).isEqualTo(api2.getLastName());
    assertThat(api.getEmail()).isEqualTo(api2.getEmail());
    assertThat(api.getPhoneNumber()).isEqualTo(api2.getPhoneNumber());

    // @formatter:off
    var apiList2 = api2.getLeases().stream().collect(Collectors.toList());
    apiList2.sort((x, y) -> x.getId() - y.getId());
    IntStream.range(0, apiList2.size())
      .mapToObj(i -> Map.entry(apiList.get(i), apiList2.get(i)))
      .collect(Collectors.toList())
      .forEach(x -> assertThat(x.getKey().getId()).isEqualTo(x.getValue().getId()));
    // @formatter:on

  }
}