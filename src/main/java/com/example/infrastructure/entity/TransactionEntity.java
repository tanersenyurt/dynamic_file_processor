package com.example.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    private String transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime transactionDate;
    private String description;
    private String source; // Bank name
    private String hashValue; // MD5 hash
} 