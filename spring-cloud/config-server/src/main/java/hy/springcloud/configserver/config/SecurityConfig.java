package hy.springcloud.configserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  SecurityFilterChain configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(requests -> requests
        .requestMatchers("/actuator/**").permitAll()
        .anyRequest()
        .authenticated())
      .httpBasic(Customizer.withDefaults());
    // @formatter:on
    return http.build();
  }
}
