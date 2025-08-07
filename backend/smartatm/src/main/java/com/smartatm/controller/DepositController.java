package com.smartatm.controller;

import com.smartatm.dto.TokenRequest;
import com.smartatm.model.DepositToken;
import com.smartatm.model.User;
import com.smartatm.repository.DepositTokenRepository;
import com.smartatm.repository.UserRepository;
import com.smartatm.security.JwtService;
import com.smartatm.service.DepositService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor

public class DepositController {

    private final DepositService depositService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    //Generate QR Token (user generates QR to deposit money)
    @PostMapping("/create-qr")
    public ResponseEntity<String> createDepositToken(@RequestParam Double amount, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String email = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User can not find."));

        DepositToken token = depositService.createDepositToken(user.getId(), amount);
        return ResponseEntity.ok(token.getToken());
    }

    //Deposit with QR Token (ATM Simülation)
    @PostMapping("/confirm")
    public ResponseEntity<String> depositWithToken(@RequestBody TokenRequest request) {
        depositService.depositWithToken(request.getToken());
        return ResponseEntity.ok("Deposit was successful");
    }
}
