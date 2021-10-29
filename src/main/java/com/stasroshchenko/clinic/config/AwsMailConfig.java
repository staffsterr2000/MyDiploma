package com.stasroshchenko.clinic.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.mail.simplemail.SimpleEmailServiceJavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
//@Profile("aws")
public class AwsMailConfig {

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService(
            @Value("${cloud.aws.credentials.access-key}") String awsAccessKey,
            @Value("${cloud.aws.credentials.secret-key}") String awsSecretKey) {

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withRegion(Regions.EU_WEST_1)
                .build();
    }

    @Bean
    public JavaMailSender javaMailSender(AmazonSimpleEmailService emailService) {
        return new SimpleEmailServiceJavaMailSender(emailService);
    }

}
