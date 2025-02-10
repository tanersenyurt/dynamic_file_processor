package com.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime transactionDate;
    private String description;
    private String source; // Bank name
    private String hashValue; // MD5 hash
} 