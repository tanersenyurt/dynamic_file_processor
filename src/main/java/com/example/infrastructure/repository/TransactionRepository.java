package com.example.infrastructure.repository;

import com.example.domain.model.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
} 