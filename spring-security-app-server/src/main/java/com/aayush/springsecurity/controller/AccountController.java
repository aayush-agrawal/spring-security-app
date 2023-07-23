package com.aayush.springsecurity.controller;

import com.aayush.springsecurity.entity.Account;
import com.aayush.springsecurity.repository.AccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private final AccountRepository accountRepository;

  public AccountController(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @GetMapping("/myAccount")
  public Account getAccountDetails(@RequestParam int id) {
    return accountRepository.findByCustomerId(id);
  }


}
