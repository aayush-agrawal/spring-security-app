package com.aayush.springsecurity.config;

import com.aayush.springsecurity.entity.Authority;
import com.aayush.springsecurity.entity.Customer;
import com.aayush.springsecurity.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserEmailPasswordAuthenticationProvider implements AuthenticationProvider {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  public UserEmailPasswordAuthenticationProvider(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
    this.customerRepository = customerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String pwd = authentication.getCredentials().toString();
    List<Customer> customer = customerRepository.findByEmail(username);
    if (customer.size() > 0) {
      if (passwordEncoder.matches(pwd, customer.get(0).getPassword())) {
        return new UsernamePasswordAuthenticationToken(username, pwd, getAuthorities(customer.get(0).getAuthorities()));
      }

      throw new BadCredentialsException("Invalid password!");

    }

    throw new BadCredentialsException("No user registered with this details!");
  }

  private List<GrantedAuthority> getAuthorities(Set<Authority> authorities) {
    return authorities
      .stream()
      .map(authority -> new SimpleGrantedAuthority(authority.getName()))
      .collect(Collectors.toList());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
