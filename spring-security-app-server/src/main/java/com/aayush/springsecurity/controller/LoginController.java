package com.aayush.springsecurity.controller;

import com.aayush.springsecurity.entity.Customer;
import com.aayush.springsecurity.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
public class LoginController {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
    this.customerRepository = customerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
    customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    customer.setCreateDt(String.valueOf(new Date(System.currentTimeMillis())));
    customerRepository.save(customer);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Given user details are successfully registered");
  }

  @RequestMapping("/user")
  public Customer getUserDetailsAfterLogin(Authentication authentication) {
    List<Customer> customers = customerRepository.findByEmail(authentication.getName());
    if (customers.size() > 0) {
      return customers.get(0);
    } else {
      return null;
    }

  }

}
