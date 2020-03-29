package com.interconn.demo.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class CloudfrontConfig {
    @Value("${aws.cloudfront.endpoint}")
    private String cdnEndpoint;

    @Bean("cdnEndpoint")
    public String getCdnEndpoint() {
        return cdnEndpoint;
    }
}
