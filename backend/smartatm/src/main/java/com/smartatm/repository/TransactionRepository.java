package com.smartatm.repository;

import com.smartatm.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromIbanOrToIban(String fromIban, String toIban);
}
