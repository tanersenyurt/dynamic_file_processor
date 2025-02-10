package com.example.infrastructure.batch;

import com.example.domain.model.Transaction;
import com.example.infrastructure.util.TransactionIdFormatter;
import org.springframework.batch.item.ItemProcessor;

public class TransactionValidator implements ItemProcessor<Transaction, Transaction> {
    
    private static final int TRANSACTION_ID_LENGTH = 12;
    
    @Override
    public Transaction process(Transaction transaction) {
        if (isValid(transaction)) {
            // Ensure transaction ID is properly formatted even after validation
            return transaction.toBuilder()
                    .transactionId(TransactionIdFormatter.formatTransactionId(transaction.getTransactionId()))
                    .build();
        }
        return null; // Skip invalid transactions
    }
    
    private boolean isValid(Transaction transaction) {
        return transaction.getTransactionId() != null &&
               transaction.getTransactionId().length() <= TRANSACTION_ID_LENGTH &&
               transaction.getAccountNumber() != null &&
               transaction.getAmount() != null &&
               transaction.getAmount().compareTo(java.math.BigDecimal.ZERO) != 0 &&
               transaction.getCurrency() != null &&
               transaction.getTransactionDate() != null;
    }
} 