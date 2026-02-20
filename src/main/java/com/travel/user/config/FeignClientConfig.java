package com.travel.user.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    /**
     * Global Feign error decoder for handling service-to-service errors
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() >= 400) {
                String body = "";
                if (response.body() != null) {
                    try {
                        body = new String(response.body().asInputStream().readAllBytes());
                    } catch (Exception e) {
                        body = "Unable to read response body";
                    }
                }
                return new RuntimeException("Service error: " + response.status() + " - " + body);
            }
            return new RuntimeException("Service call failed");
        };
    }

    /**
     * Add internal API key to all Feign requests
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // Add internal service-to-service authentication header
            String internalApiKey = System.getenv("INTERNAL_API_KEY");
            if (internalApiKey == null) {
                internalApiKey = "internal-service-key"; // Default for development
            }
            template.header("X-Internal-API-Key", internalApiKey);
            template.header("X-Service-Name", "user-service");
        };
    }
}

