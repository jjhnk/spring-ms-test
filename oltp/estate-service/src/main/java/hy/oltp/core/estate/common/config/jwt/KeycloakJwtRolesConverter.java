package hy.oltp.core.estate.common.config.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeycloakJwtRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String ROLE_PREFIX = "ROLE_";
  private static final String CLAIM_REALM_ACCESS = "realm_access";
  private static final String CLAIM_RESOURCE_ACCESS = "resource_access";
  private static final String CLAIM_ROLES = "roles";
  private static final String CLAIM_CLIENT_ID = "estate-service";

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    String scopes = jwt.getClaim("scope");
    for (var scope : scopes.split(" ")) {
      grantedAuthorities.add(new SimpleGrantedAuthority(scope));
    }

    Map<String, Collection<String>> realmAccess = jwt.getClaim(CLAIM_REALM_ACCESS);
    if (realmAccess == null || realmAccess.isEmpty()) {
      return grantedAuthorities;
    }

    Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim(CLAIM_RESOURCE_ACCESS);
    if (resourceAccess == null || resourceAccess.isEmpty()) {
      return grantedAuthorities;
    }

    Map<String, Collection<String>> clientAccess = resourceAccess.get(CLAIM_CLIENT_ID);
    if (clientAccess == null || clientAccess.isEmpty()) {
      return grantedAuthorities;
    }

    Collection<String> clientRoles = clientAccess.get(CLAIM_ROLES);
    if (clientRoles != null && !clientRoles.isEmpty()) {
      return grantedAuthorities;
    }

    clientRoles.forEach(r -> log.info("Role: {}", r));
    clientRoles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));

    return grantedAuthorities;
  }

  @Override
  public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
    return Converter.super.andThen(after);
  }
}
