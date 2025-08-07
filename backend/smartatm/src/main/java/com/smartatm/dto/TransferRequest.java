package com.smartatm.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private String toIban;
    private double amount;
}
