package com.example.infrastructure.repository;

import com.example.infrastructure.entity.BankConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankConfigRepository extends JpaRepository<BankConfigEntity, Long> {
    BankConfigEntity findByBankCode(String bankCode);
} 