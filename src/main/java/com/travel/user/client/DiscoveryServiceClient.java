package com.travel.user.client;

import com.travel.user.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

/**
 * Feign client for calling Discovery Service
 */
@FeignClient(
        name = "discovery-service",
        url = "${discovery-service.url:http://localhost:8082}"
)
public interface DiscoveryServiceClient {

    /**
     * Get destination by ID from Discovery Service
     */
    @GetMapping("/destinations/{id}")
    ApiResponse<?> getDestination(@PathVariable UUID id);

    /**
     * Check if discovery service is healthy
     */
    @GetMapping("/actuator/health")
    Map<String, Object> health();
}

