package com.example.infrastructure.batch;

import com.example.domain.model.Transaction;
import com.example.infrastructure.repository.TransactionRepository;
import com.example.infrastructure.entity.BankConfigEntity;
import com.example.infrastructure.repository.BankConfigRepository;
import com.example.infrastructure.service.DynamicFieldMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DynamicBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TransactionRepository transactionRepository;
    private final BankConfigRepository bankConfigRepository;
    private final DynamicFieldMapperService fieldMapperService;

    @Bean
    public Job transactionJob(Step transactionStep) {
        return new JobBuilder("transactionJob", jobRepository)
                .start(transactionStep)
                .build();
    }

    @Bean
    public Step transactionStep() {
        return new StepBuilder("transactionStep", jobRepository)
                .<Transaction, Transaction>chunk(1000, transactionManager)
                .reader(reader(null, null))
                .processor(new TransactionValidator())
                .writer(items -> items.forEach(transactionRepository::save))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> reader(
            @Value("#{jobParameters['inputFile']}") String filePath,
            @Value("#{jobParameters['bankCode']}") String bankCode) {
        
        BankConfigEntity bankConfig = bankConfigRepository.findByBankCode(bankCode);
        
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionReader")
                .resource(new PathResource(filePath))
                .linesToSkip(1)
                .delimited()
                .delimiter(bankConfig.getCsvDelimiter())
                .names(getFieldNames(bankConfig))
                .fieldSetMapper(fieldMapperService.createFieldMapper(bankConfig))
                .build();
    }

    private String[] getFieldNames(BankConfigEntity bankConfig) {
        // Parse csvFormat JSON from bankConfig to get field names
        // This is a simplified example - you'll need to implement the actual JSON parsing
        return bankConfig.getCsvFormat().split(",");
    }
} 