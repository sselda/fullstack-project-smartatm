package com.smartatm.repository;

import com.smartatm.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);
    Optional<Account> findByIban(String iban);
}
