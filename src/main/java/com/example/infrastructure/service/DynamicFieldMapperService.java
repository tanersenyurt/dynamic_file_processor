package com.example.infrastructure.service;

import com.example.domain.model.Transaction;
import com.example.infrastructure.entity.BankConfigEntity;
import com.example.infrastructure.entity.CsvFieldMappingEntity;
import com.example.infrastructure.repository.CsvFieldMappingRepository;
import com.example.infrastructure.util.HashGenerator;
import com.example.infrastructure.util.NumberFormatter;
import com.example.infrastructure.util.TransactionIdFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DynamicFieldMapperService {
    private final CsvFieldMappingRepository mappingRepository;

    public FieldSetMapper<Transaction> createFieldMapper(BankConfigEntity bankConfig) {
        List<CsvFieldMappingEntity> mappings = mappingRepository.findByBankConfigId(bankConfig.getId());
        Map<String, CsvFieldMappingEntity> mappingMap = mappings.stream()
                .collect(Collectors.toMap(CsvFieldMappingEntity::getSourceField, mapping -> mapping));

        return fieldSet -> {
            Transaction.TransactionBuilder builder = Transaction.builder();
            List<String> hashValues = new ArrayList<>();
            
            // Set source (bank name)
            builder.source(bankConfig.getBankName());
            
            // Get fields for hash calculation
            String[] hashFields = bankConfig.getHashFields().split(",");
            
            for (String fieldName : fieldSet.getNames()) {
                CsvFieldMappingEntity mapping = mappingMap.get(fieldName);
                if (mapping != null) {
                    setField(builder, mapping, fieldSet);
                    
                    // Collect values for hash fields
                    if (Arrays.asList(hashFields).contains(fieldName)) {
                        hashValues.add(fieldSet.readString(fieldName));
                    }
                }
            }
            
            // Generate and set hash value
            builder.hashValue(HashGenerator.generateMd5Hash(hashValues));
            
            return builder.build();
        };
    }

    private void setField(Transaction.TransactionBuilder builder, CsvFieldMappingEntity mapping, FieldSet fieldSet) {
        String sourceField = mapping.getSourceField();
        String targetField = mapping.getTargetField();
        
        switch (mapping.getDataType().toUpperCase()) {
            case "STRING":
                setStringField(builder, targetField, fieldSet.readString(sourceField));
                break;
            case "NUMBER":
                setBigDecimalField(builder, targetField, fieldSet.readString(sourceField), mapping);
                break;
            case "DATE":
                setDateField(builder, targetField, fieldSet.readString(sourceField), mapping.getDateFormat());
                break;
        }
    }

    private void setStringField(Transaction.TransactionBuilder builder, String fieldName, String value) {
        switch (fieldName) {
            case "transactionId" -> builder.transactionId(TransactionIdFormatter.formatTransactionId(value));
            case "accountNumber" -> builder.accountNumber(value);
            case "currency" -> builder.currency(value);
            case "description" -> builder.description(value);
        }
    }

    private void setBigDecimalField(Transaction.TransactionBuilder builder, String fieldName, 
                                  String value, CsvFieldMappingEntity mapping) {
        if ("amount".equals(fieldName)) {
            BigDecimal parsedValue = NumberFormatter.parseBigDecimal(
                value,
                mapping.getDecimalSeparator(),
                mapping.getGroupingSeparator(),
                mapping.getDecimalPrecision()
            );
            builder.amount(parsedValue);
        }
    }

    private void setDateField(Transaction.TransactionBuilder builder, String fieldName, String value, String format) {
        if ("transactionDate".equals(fieldName)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            builder.transactionDate(LocalDate.parse(value, formatter).atStartOfDay());
        }
    }
} 