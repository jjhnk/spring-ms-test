package hy.oltp.core.estate.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private static final String AUTHORITY_ESTATE = "estate";
  private static final String ESTATE_SERVICE_URL = "/estate/**";

  @Value("${spring.websecurity.debug:false}")
  boolean webSecurityDebug;

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(sessionRegistry());
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter)
    throws Exception {
    return http.csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
      // @formatter:off
        .requestMatchers("/estate/openapi/**").permitAll()
        .requestMatchers("/actuator/**").permitAll()
        .requestMatchers(HttpMethod.POST, ESTATE_SERVICE_URL).hasAuthority(AUTHORITY_ESTATE)
        .requestMatchers(HttpMethod.GET, ESTATE_SERVICE_URL).hasAuthority(AUTHORITY_ESTATE)
        .requestMatchers(HttpMethod.PUT, ESTATE_SERVICE_URL).hasAuthority(AUTHORITY_ESTATE)
        .requestMatchers(HttpMethod.DELETE, ESTATE_SERVICE_URL).hasAuthority(AUTHORITY_ESTATE)
        .anyRequest().authenticated()
        // @formatter:on
      )
      .oauth2ResourceServer(
        resourceServer -> resourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
      .build();
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.debug(webSecurityDebug);
  }

}
