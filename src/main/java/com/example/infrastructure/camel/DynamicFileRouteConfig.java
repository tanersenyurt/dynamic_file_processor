package com.example.infrastructure.camel;

import com.example.infrastructure.entity.BankConfigEntity;
import com.example.infrastructure.repository.BankConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DynamicFileRouteConfig extends RouteBuilder {
    
    private final JobLauncher jobLauncher;
    private final Job transactionJob;
    private final BankConfigRepository bankConfigRepository;

    @PostConstruct
    public void initializeRoutes() throws Exception {
        List<BankConfigEntity> bankConfigs = bankConfigRepository.findAll();
        for (BankConfigEntity config : bankConfigs) {
            createRouteForBank(config);
        }
    }

    private void createRouteForBank(BankConfigEntity config) {
        String ftpUrl = String.format("ftp://%s:%s%s?username=%s&password=%s&delete=false",
                config.getFtpHost(),
                config.getFtpPort(),
                config.getRemotePath(),
                config.getFtpUsername(),
                config.getFtpPassword());

        String fileURl = String.format("file:input?fileName=%s&noop=true",config.getRemotePath());
        from(fileURl)
            .routeId("ftpRoute-" + config.getBankCode())
            .to("file:data/output/" + config.getBankCode())
            .process(exchange -> {
                String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("inputFile", "data/output/" + config.getBankCode() + "/" + fileName)
                        .addString("bankCode", config.getBankCode())
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();
                jobLauncher.run(transactionJob, jobParameters);
            });
    }

    @Override
    public void configure() throws Exception {
        // Routes are created dynamically in initializeRoutes()
    }
} 