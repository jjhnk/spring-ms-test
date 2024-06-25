package hy.springcloud.authorizationserver.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
@Configuration
@EnableWebSecurity
@Profile("!test")
public class DefaultSecurityConfig {
  // formatter:off
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .requestMatchers("/actuator/**").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(withDefaults());
    return http.build();
  }
  // formatter:on

  // @formatter:off
  @Bean
  UserDetailsService users() {
    UserDetails user = User.withDefaultPasswordEncoder()
      .username("u")
      .password("p")
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }
  // @formatter:on

  // @Bean
  // PasswordEncoder passwordEncoder() {
  //   return new BCryptPasswordEncoder();
  // }

}
//CHECKSTYLE:ON