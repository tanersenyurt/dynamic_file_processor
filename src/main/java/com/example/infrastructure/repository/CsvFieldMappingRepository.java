package com.example.infrastructure.repository;

import com.example.infrastructure.entity.CsvFieldMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CsvFieldMappingRepository extends JpaRepository<CsvFieldMappingEntity, Long> {
    List<CsvFieldMappingEntity> findByBankConfigId(Long bankConfigId);
} 