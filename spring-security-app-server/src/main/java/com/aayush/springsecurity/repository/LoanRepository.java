package com.aayush.springsecurity.repository;

import com.aayush.springsecurity.entity.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {

  @PreAuthorize("hasRole('USER')")
  List<Loan> findByCustomerIdOrderByStartDtDesc(int customerId);

}