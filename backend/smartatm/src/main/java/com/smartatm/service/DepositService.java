package com.smartatm.service;

import com.smartatm.model.Account;
import com.smartatm.model.DepositToken;
import com.smartatm.model.Transaction;
import com.smartatm.model.TransactionType;
import com.smartatm.repository.AccountRepository;
import com.smartatm.repository.DepositTokenRepository;
import com.smartatm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DepositService {

    private final AccountRepository accountRepository;
    private final DepositTokenRepository depositTokenRepository;
    private final TransactionRepository transactionRepository;

    public DepositToken createDepositToken(Long userId, Double amount) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account can not find."));

        DepositToken token = DepositToken.builder()
                .token(UUID.randomUUID().toString())
                .amount(amount)
                .used(false)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .account(account)
                .build();

        return depositTokenRepository.save(token);
    }

    public void depositWithToken(String tokenString) {
        DepositToken token = depositTokenRepository.findByTokenAndUsedFalse(tokenString)
                .orElseThrow(() -> new RuntimeException("Token invalid."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token is expired.");
        }

        Account account = token.getAccount();
        account.setBalance(account.getBalance() + token.getAmount());
        token.setUsed(true);

        accountRepository.save(account);
        depositTokenRepository.save(token);

        Transaction transaction = new Transaction();
        transaction.setFromIban(null);
        transaction.setToIban(account.getIban());
        transaction.setAmount(token.getAmount()); //Money coming from the ATM
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);

        transactionRepository.save(transaction);
    }
}
