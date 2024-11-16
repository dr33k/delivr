package com.seven.delivr.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfiguration {
    @Value("${aws.access.key.id}")
    private String accessKeyId;
    @Value("${aws.access.key.secret}")
    private String accessKeySecret;
    @Value("${aws.s3.bucket.region}")
    private String regionName;
    @Bean
    public AmazonS3 s3Client(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionName)
                .build();
    }
}