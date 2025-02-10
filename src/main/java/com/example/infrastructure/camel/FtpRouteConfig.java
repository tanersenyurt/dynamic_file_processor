package com.example.infrastructure.camel;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class FtpRouteConfig extends RouteBuilder {
    
    private final JobLauncher jobLauncher;
    private final Job transactionJob;
    
    @Value("${ftp.host}")
    private String ftpHost;
    
    @Value("${ftp.port}")
    private String ftpPort;
    
    @Value("${ftp.username}")
    private String username;
    
    @Value("${ftp.password}")
    private String password;
    
    @Override
    public void configure() throws Exception {
        from("ftp://" + ftpHost + ":" + ftpPort + "/transactions?" +
                "username=" + username + "&password=" + password + "&delete=false")
        .routeId("ftpRoute")
        .to("file:data/input")
        .process(exchange -> {
            String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("inputFile", "data/input/" + fileName)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(transactionJob, jobParameters);
        });
    }
} 