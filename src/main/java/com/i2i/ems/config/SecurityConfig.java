package com.i2i.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * <p>
 * Configures the security settings for the application.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final static int STRENGTH = 12;
  private final static String[] WHITELIST = {
      "/v1/employees/login",
      "/v1/employees/register"
  };

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver handlerExceptionResolver;

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public JwtTokenFilter jwtTokenFilter() {
    return new JwtTokenFilter(handlerExceptionResolver);
  }

  /**
   * <p>
   * Configures the security filter chain that carries out authentication and authorization.
   * </p>
   *
   * @param http the {@link HttpSecurity} object to be configured.
   * @return {@link SecurityFilterChain} custom security filter chain object to be created as a bean.
   * @throws Exception if any error occurs during the configuration.
   */
  @Bean
  protected SecurityFilterChain configureSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(customizer -> customizer
            .requestMatchers(WHITELIST).permitAll()
            .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * <p>
   * Configures the authentication provider.
   * </p>
   *
   * @return {@link AuthenticationProvider} custom authentication provider.
   */
  @Bean
  public AuthenticationProvider configureAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(new BCryptPasswordEncoder(STRENGTH));
    return authProvider;
  }

  /**
   * <p>
   * Configures the authentication manager.
   * </p>
   *
   * @param http {@link HttpSecurity} object to be configured.
   * @return {@link AuthenticationManager} custom authentication manager.
   * @throws Exception if any error occurs during the configuration.
   */
  @Bean
  public AuthenticationManager configureAuthenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.eraseCredentials(false)
        .userDetailsService(userDetailsService)
        .passwordEncoder(configurePasswordEncoder());
    return authenticationManagerBuilder.build();
  }

  /**
   * <p>
   * Configures the password encoder.
   * </p>
   *
   * @return {@link BCryptPasswordEncoder}custom password encoder with strength.
   */
  @Bean
  public BCryptPasswordEncoder configurePasswordEncoder() {
    return new BCryptPasswordEncoder(STRENGTH);
  }
}