package com.example.infrastructure.config;

import com.example.infrastructure.entity.BankConfigEntity;
import com.example.infrastructure.entity.CsvFieldMappingEntity;
import com.example.infrastructure.repository.BankConfigRepository;
import com.example.infrastructure.repository.CsvFieldMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BankConfigRepository bankConfigRepository;
    private final CsvFieldMappingRepository mappingRepository;

    @Override
    public void run(String... args) {
        // Create Bank1 configuration

        BankConfigEntity bank1 = BankConfigEntity.builder()
                .bankCode("BANK1")
                .bankName("First Bank")
                .ftpHost("ftp1.example.com")
                .ftpPort("21")
                .ftpUsername("user1")
                .ftpPassword("pass1")
                .remotePath("bank1.csv")
                .csvFormat("ref,account,amount,curr,date,desc")
                .hashFields("ref,account,amount")
                .build();

        final BankConfigEntity foundedBank1 = bankConfigRepository.findByBankCode(bank1.getBankCode());
        if(foundedBank1 == null){
            bank1 = bankConfigRepository.save(bank1);

            // Create Bank1 field mappings
            createFieldMapping(bank1, "ref", "transactionId", "STRING", null, null, null, null, null);
            createFieldMapping(bank1, "account", "accountNumber", "STRING", null, null, null, null, null);
            createFieldMapping(bank1, "amount", "amount", "NUMBER", null, "#,##0.00", 2, ".", ",");
            createFieldMapping(bank1, "curr", "currency", "STRING", null, null, null, null, null);
            createFieldMapping(bank1, "date", "transactionDate", "DATE", "yyyy-MM-dd HH:mm:ss", null, null, null, null);
            createFieldMapping(bank1, "desc", "description", "STRING", null, null, null, null, null);

            // Create Bank2 configuration
        }
        BankConfigEntity bank2 = BankConfigEntity.builder()
                .bankCode("BANK2")
                .bankName("Second Bank")
                .ftpHost("ftp2.example.com")
                .ftpPort("21")
                .ftpUsername("user2")
                .ftpPassword("pass2")
                .remotePath("bank2.csv")
                .csvFormat("transaction_id,acc_num,transaction_amount,currency_code,value_date,narrative")
                .hashFields("transaction_id,transaction_amount")
                .build();

        final BankConfigEntity foundedBank2 = bankConfigRepository.findByBankCode(bank2.getBankCode());
       if(foundedBank2 == null){
           bank2 = bankConfigRepository.save(bank2);

           // Create Bank2 field mappings
           createFieldMapping(bank2, "transaction_id", "transactionId", "STRING", null, null, null, null, null);
           createFieldMapping(bank2, "acc_num", "accountNumber", "STRING", null, null, null, null, null);
           createFieldMapping(bank2, "transaction_amount", "amount", "NUMBER", null, "#.##0,00", 2, ",", ".");
           createFieldMapping(bank2, "currency_code", "currency", "STRING", null, null, null, null, null);
           createFieldMapping(bank2, "value_date", "transactionDate", "DATE", "dd/MM/yyyy", null, null, null, null);
           createFieldMapping(bank2, "narrative", "description", "STRING", null, null, null, null, null);

       }

        // Create Bank3 configuration (semicolon-delimited)
        BankConfigEntity bank3 = BankConfigEntity.builder()
                .bankCode("BANK3")
                .bankName("Third Bank")
                .ftpHost("ftp3.example.com")
                .ftpPort("21")
                .ftpUsername("user3")
                .ftpPassword("pass3")
                .remotePath("bank3.csv")
                .csvFormat("transaction_reference;account_id;transaction_value;currency;transaction_datetime;notes")
                .hashFields("transaction_reference,transaction_value")
                .csvDelimiter(";")  // Set semicolon delimiter
                .build();
        final BankConfigEntity foundedBank3 = bankConfigRepository.findByBankCode(bank3.getBankCode());
        if(foundedBank3 == null){
            bank3 = bankConfigRepository.save(bank3);

            // Create Bank3 field mappings
            createFieldMapping(bank3, "transaction_reference", "transactionId", "STRING", null, null, null, null, null);
            createFieldMapping(bank3, "account_id", "accountNumber", "STRING", null, null, null, null, null);
            createFieldMapping(bank3, "transaction_value", "amount", "NUMBER", null, "#.##0,00", 2, ",", "."); // European format
            createFieldMapping(bank3, "currency", "currency", "STRING", null, null, null, null, null);
            createFieldMapping(bank3, "transaction_datetime", "transactionDate", "DATE", "dd-MM-yyyy HH:mm:ss", null, null, null, null);
            createFieldMapping(bank3, "notes", "description", "STRING", null, null, null, null, null);

        }
  }

    private void createFieldMapping(BankConfigEntity bankConfig, 
                                  String sourceField, 
                                  String targetField,
                                  String dataType,
                                  String dateFormat,
                                  String numberFormat,
                                  Integer decimalPrecision,
                                  String decimalSeparator,
                                  String groupingSeparator) {
        CsvFieldMappingEntity mapping = CsvFieldMappingEntity.builder()
                .bankConfig(bankConfig)
                .sourceField(sourceField)
                .targetField(targetField)
                .dataType(dataType)
                .dateFormat(dateFormat)
                .numberFormat(numberFormat)
                .decimalPrecision(decimalPrecision)
                .decimalSeparator(decimalSeparator)
                .groupingSeparator(groupingSeparator)
                .build();
        mappingRepository.save(mapping);
    }
} 