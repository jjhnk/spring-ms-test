package hy.springcloud.authorizationserver;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestSecurityConfig {

  @Bean
  @Profile("test")
  UserDetailsService testUsers() {
    UserDetails user = User.builder()
      .username("u")
      .password("p")
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }
}
