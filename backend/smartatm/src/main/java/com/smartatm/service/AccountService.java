package com.smartatm.service;

import com.smartatm.model.Account;
import com.smartatm.model.User;
import com.smartatm.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccountForUser(User user) {
        Account account = Account.builder()
                .iban(UUID.randomUUID().toString().substring(0,16))
                .balance(1000.0)
                .user(user)
                .build();

        return accountRepository.save(account);
    }

    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account can not find"));
    }

}
