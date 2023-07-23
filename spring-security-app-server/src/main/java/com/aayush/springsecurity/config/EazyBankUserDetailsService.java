package com.aayush.springsecurity.config;

import com.aayush.springsecurity.entity.Customer;
import com.aayush.springsecurity.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EazyBankUserDetailsService implements UserDetailsService {
  private final CustomerRepository customerRepository;

  public EazyBankUserDetailsService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<Customer> customers = customerRepository.findByEmail(username);
    if(customers.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    return new User(customers.get(0).getEmail(), customers.get(0).getPassword(), List.of(new SimpleGrantedAuthority(customers.get(0).getRole())));
  }
}
