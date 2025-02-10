package com.example.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bankCode;
    private String bankName;
    private String ftpHost;
    private String ftpPort;
    private String ftpUsername;
    private String ftpPassword;
    private String remotePath;
    
    @Column(columnDefinition = "TEXT")
    private String csvFormat; // JSON format containing field mappings
    
    @Column(columnDefinition = "TEXT")
    private String hashFields; // Comma-separated list of fields to be used in hash generation
    
    @Column(length = 1)
    private String csvDelimiter = ","; // Default delimiter is comma
} 