package com.aayush.springsecurity.config;

import com.aayush.springsecurity.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf"); // default is _csrf


    http
//      .securityContext(contextConfigurer ->
        // generates the JSESSIONID and stores it in the securityContextHolder
        // if true then it needs to be saved explicitly
//        contextConfigurer.requireExplicitSave(false)
//      )
//      .sessionManagement(sessionManagementConfigurer->
          // indicates that a session should always be created for each request, even if it's not necessary.
//          sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//      )
      .sessionManagement( sessionManagementConfigurer ->
        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .cors(httpSecurityCorsConfigurer ->
        httpSecurityCorsConfigurer.configurationSource(request-> {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
          configuration.setAllowedMethods(Collections.singletonList("*"));
          configuration.setAllowCredentials(true);
          configuration.setAllowedHeaders(Collections.singletonList("*"));
          configuration.setMaxAge(3600l);
          configuration.setExposedHeaders(Arrays.asList("Authorization")); // added for JWT
          return configuration;
        })
      )
      .csrf(httpSecurityCsrfConfigurer ->
        httpSecurityCsrfConfigurer
          .csrfTokenRequestHandler(requestHandler)
          .ignoringRequestMatchers("/contact", "/register")
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      )
      .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
      .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
      .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
      .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
      .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
      .addFilterAfter(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
      .authorizeHttpRequests( requests ->
        requests
//          .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
//          .requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
//          .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
//          .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
          .requestMatchers("/myAccount").hasRole("USER")
          .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
          .requestMatchers("/myLoans").authenticated() // configured method level security
          .requestMatchers("/myCards").hasRole("USER")
          .requestMatchers("/user").authenticated()
          .requestMatchers("/notices", "contact", "/register").permitAll()
      )
      //  It enables the form-based login functionality for your application,
      //  allowing users to authenticate by submitting their credentials through an HTML form.
      .formLogin(Customizer.withDefaults())
      // configures HTTP Basic authentication which is a simple authentication mechanism
      // where the client sends the username and password as part of the HTTP request headers.
      // adds a BasicAuthenticationFilter to the security filter chain.
      // This filter intercepts incoming requests and looks for the presence of the Authorization header with the Basic authentication scheme.
      // If the header is found, the filter extracts the username and password from it.
      .httpBasic(Customizer.withDefaults());
    return http.build();
  }

//  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setMaxAge(3600l);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

//  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsService() {
    UserDetails user1 = User.withUsername("user1")
      .password("password1")
      .roles("USER")
      .build();

    UserDetails user2 = User.withUsername("user2")
      .password("password2")
      .roles("USER")
      .build();

    return new InMemoryUserDetailsManager(user1, user2);
  }

//  @Bean
  public UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
