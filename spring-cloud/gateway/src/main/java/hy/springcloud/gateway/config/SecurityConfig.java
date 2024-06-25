package hy.springcloud.gateway.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf(CsrfSpec::disable)
      .authorizeExchange(exchange -> exchange
        .pathMatchers("/headerrouting/**").permitAll()
        .pathMatchers("/actuator/**").permitAll()
        .pathMatchers("/config/**").permitAll()
        .pathMatchers("/oauth2/**").permitAll()
        .pathMatchers("/login/**").permitAll()
        .pathMatchers("/error/**").permitAll()
        .pathMatchers("/openapi/**").permitAll()
        .pathMatchers("/webjars/**").permitAll()
        .anyExchange().authenticated())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
    // @formatter:on
    return http.build();
  }
}
