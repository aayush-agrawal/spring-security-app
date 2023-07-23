package com.aayush.springsecurity.controller;

import com.aayush.springsecurity.entity.Loan;
import com.aayush.springsecurity.repository.LoanRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoansController {

  private final LoanRepository loanRepository;

  public LoansController(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @GetMapping("/myLoans")
  public List<Loan> getLoanDetails(@RequestParam int id) {
    return loanRepository.findByCustomerIdOrderByStartDtDesc(id);
  }

}
