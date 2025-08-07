package com.smartatm.service;

import com.smartatm.dto.TransferRequest;
import com.smartatm.model.Account;
import com.smartatm.model.Transaction;
import com.smartatm.model.TransactionType;
import com.smartatm.model.User;
import com.smartatm.repository.AccountRepository;
import com.smartatm.repository.TransactionRepository;
import com.smartatm.repository.UserRepository;
import com.smartatm.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transferMoney(String token, TransferRequest request) {
        String email = jwtService.extractUsername(token);
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User can not found"));

        Account fromAccount = accountRepository.findByUserId(sender.getId())
                .orElseThrow(() -> new RuntimeException("Sender Account can not found"));

        Account toAccount = accountRepository.findByIban(request.getToIban())
                .orElseThrow(() -> new RuntimeException("Recipient IBAN can not found"));

        if(fromAccount.getBalance() < request.getAmount()) {
            throw new RuntimeException("Unsufficient Balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
        toAccount.setBalance(toAccount.getBalance() + request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromIban(fromAccount.getIban());
        transaction.setToIban(toAccount.getIban());
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.TRANSFER);

        transactionRepository.save(transaction);
    }
}
