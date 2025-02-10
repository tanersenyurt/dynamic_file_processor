package com.example.infrastructure.adapter;

import com.example.domain.model.Transaction;
import com.example.infrastructure.repository.TransactionRepository;
import com.example.infrastructure.entity.TransactionEntity;
import com.example.infrastructure.repository.TransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {
    private final TransactionJpaRepository jpaRepository;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapToEntity(transaction);
        TransactionEntity savedEntity = jpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    private TransactionEntity mapToEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .transactionId(transaction.getTransactionId())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .hashValue(transaction.getHashValue())
                .source(transaction.getSource())
                .build();
    }

    private Transaction mapToDomain(TransactionEntity entity) {
        return Transaction.builder()
                .transactionId(entity.getTransactionId())
                .accountNumber(entity.getAccountNumber())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .transactionDate(entity.getTransactionDate())
                .description(entity.getDescription())
                .hashValue(entity.getHashValue())
                .source(entity.getSource())
                .build();
    }
} 