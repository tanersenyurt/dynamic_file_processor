package com.example.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "csv_field_mappings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvFieldMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bank_config_id")
    private BankConfigEntity bankConfig;
    
    private String sourceField; // CSV column name
    private String targetField; // Transaction entity field name
    private String dataType; // STRING, NUMBER, DATE
    private String dateFormat; // For date fields
    private String numberFormat; // For number fields (e.g., "#,##0.00" or "#.##0,00")
    private Integer decimalPrecision; // Number of decimal places for BigDecimal
    private String decimalSeparator; // "." or "," 
    private String groupingSeparator; // "," or "."
} 