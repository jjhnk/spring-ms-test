package hy.springcloud.eurekaserver.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  private final String username;
  private final String password;

  public SecurityConfig(@Value("${app.eureka.username}") String username,
    @Value("${app.eureka.password}") String password) {
    this.username = username;
    this.password = password;
  }

  @Bean
  InMemoryUserDetailsManager userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
      .username(username)
      .password(password)
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }

  // @Bean
  // PasswordEncoder passwordEncoder() {
  //   return new BCryptPasswordEncoder();
  // }

  @Bean
  SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(requests -> requests.anyRequest()
        .authenticated())
      .httpBasic(withDefaults());
    return http.build();
  }
}
