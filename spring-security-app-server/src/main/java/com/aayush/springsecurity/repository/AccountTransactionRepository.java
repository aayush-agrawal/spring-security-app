package com.aayush.springsecurity.repository;

import com.aayush.springsecurity.entity.AccountTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {

  List<AccountTransaction> findByCustomerIdOrderByTransactionDtDesc(int customerId);

}
