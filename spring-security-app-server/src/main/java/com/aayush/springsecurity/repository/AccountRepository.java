package com.aayush.springsecurity.repository;

import com.aayush.springsecurity.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

  Account findByCustomerId(int customerId);

}
