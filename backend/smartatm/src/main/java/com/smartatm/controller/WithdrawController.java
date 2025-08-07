package com.smartatm.controller;

import com.smartatm.dto.TokenRequest;
import com.smartatm.model.User;
import com.smartatm.model.WithdrawToken;
import com.smartatm.repository.UserRepository;
import com.smartatm.security.JwtService;
import com.smartatm.service.WithdrawService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/withdraw")
@RequiredArgsConstructor

public class WithdrawController {

    private final WithdrawService withdrawService;
    @Autowired
    private JwtService jwtService;
    private final UserRepository userRepository;

    //Created QR Token endpoint
    @PostMapping("/create-qr")
    public ResponseEntity<String> createWithdrawToken(@RequestParam Double amount, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User can not find"));

        WithdrawToken withdrawToken = withdrawService.createWithdrawToken(user.getId(), amount);
        return ResponseEntity.ok(withdrawToken.getToken());
    }

    // Endpoint for withdrawing money with QR Token (like ATM)
    @PostMapping("/confirm")
    public ResponseEntity<String> withdrawToken(@RequestBody TokenRequest request) {
        withdrawService.withdrawWithToken(request.getToken());
        return ResponseEntity.ok("Withdrawal completed successfully.");
    }
}
