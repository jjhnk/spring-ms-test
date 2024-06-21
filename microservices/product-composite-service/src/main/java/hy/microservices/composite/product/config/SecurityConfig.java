package hy.microservices.composite.product.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  private static final String SERVICE_URL = "/product-composite/**";

  // public SecurityConfig(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
  //   this.issuerUri = issuerUri;
  // }
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable)
      .authorizeExchange(exchange ->
        exchange
          .pathMatchers("/openapi/**").permitAll()
          .pathMatchers("/webjars/**").permitAll()
          .pathMatchers("/actuator/**").permitAll()
          .pathMatchers(POST, SERVICE_URL).hasAuthority("SCOPE_product:write")
          .pathMatchers(DELETE, SERVICE_URL).hasAuthority("SCOPE_product:write")
          .pathMatchers(GET, SERVICE_URL).hasAuthority("SCOPE_product:read")
          .anyExchange().authenticated()
      )
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
    return http.build();
  }

  // @Bean
  // ReactiveJwtDecoder jwtDecoder() {
  //     return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
  // }
}