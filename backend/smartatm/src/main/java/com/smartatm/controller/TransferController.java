package com.smartatm.controller;

import com.smartatm.dto.TransferRequest;
import com.smartatm.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor

public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        transferService.transferMoney(token, request);
        return ResponseEntity.ok("Transfer is successfully");
    }
}
