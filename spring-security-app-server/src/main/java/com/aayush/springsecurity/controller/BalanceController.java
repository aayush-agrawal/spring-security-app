package com.aayush.springsecurity.controller;

import com.aayush.springsecurity.entity.AccountTransaction;
import com.aayush.springsecurity.repository.AccountTransactionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

  private final AccountTransactionRepository accountTransactionRepository;

  public BalanceController(AccountTransactionRepository accountTransactionRepository) {
    this.accountTransactionRepository = accountTransactionRepository;
  }

  @GetMapping("/myBalance")
  public List<AccountTransaction> getBalanceDetails(@RequestParam int id) {
    return accountTransactionRepository.findByCustomerIdOrderByTransactionDtDesc(id);
  }

}
