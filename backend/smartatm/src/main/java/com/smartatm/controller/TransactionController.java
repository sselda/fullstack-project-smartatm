package com.smartatm.controller;

import com.smartatm.model.Transaction;
import com.smartatm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor

public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/{iban}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String iban) {
        List<Transaction> transactions = transactionRepository.findByFromIbanOrToIban(iban, iban);
        return ResponseEntity.ok(transactions);
    }
}
