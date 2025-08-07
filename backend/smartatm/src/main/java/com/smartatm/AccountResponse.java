package com.smartatm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AccountResponse {
    private String iban;
    private Double balance;
}
