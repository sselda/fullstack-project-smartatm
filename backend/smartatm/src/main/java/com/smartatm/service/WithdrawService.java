package com.smartatm.service;

import com.smartatm.model.*;
import com.smartatm.repository.AccountRepository;
import com.smartatm.repository.TransactionRepository;
import com.smartatm.repository.WithdrawTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class WithdrawService {

    private final AccountRepository accountRepository;
    private final WithdrawTokenRepository withdrawTokenRepository;
    private final TransactionRepository transactionRepository;

    public WithdrawToken createWithdrawToken(Long userId, Double amount) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account can not find"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("unsufficient balance");
        }

        WithdrawToken token = WithdrawToken.builder()
                .token(UUID.randomUUID().toString())
                .amount(amount)
                .used(false)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .account(account)
                .build();
        return withdrawTokenRepository.save(token);
    }

    public void withdrawWithToken (String tokenString) {
        WithdrawToken token = withdrawTokenRepository.findByTokenAndUsedFalse(tokenString)
                .orElseThrow(() -> new RuntimeException("Unsufficient or Used QR"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("QR has expired");
        }

        Account account = token.getAccount();
        if (account.getBalance() < token.getAmount()) {
            throw new RuntimeException("Unsufficient Balance");
        }

        //withdraw money
        account.setBalance(account.getBalance() - token.getAmount());
        token.setUsed(true);

        accountRepository.save(account);
        withdrawTokenRepository.save(token);

        Transaction transaction = new Transaction();
        transaction.setFromIban(account.getIban());
        transaction.setToIban(null); //The money goes up to the ATM
        transaction.setAmount(token.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.WITHDRAW);

        transactionRepository.save(transaction);
    }
}
