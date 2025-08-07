package com.smartatm.controller;

import com.smartatm.AccountResponse;
import com.smartatm.model.Account;
import com.smartatm.model.User;
import com.smartatm.repository.AccountRepository;
import com.smartatm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor

public class AccountController {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMyAccount(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User can not found"));
        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account can not found"));

        return ResponseEntity.ok(new AccountResponse(account.getIban(), account.getBalance()));

    }
}
