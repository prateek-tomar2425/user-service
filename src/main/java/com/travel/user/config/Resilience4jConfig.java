package com.travel.user.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    private static final Logger logger = LoggerFactory.getLogger(Resilience4jConfig.class);

    /**
     * Circuit Breaker configuration for service calls
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .recordExceptions(Exception.class)
                .ignoreExceptions(IllegalArgumentException.class)
                .failureRateThreshold(50.0f)
                .slowCallRateThreshold(50.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .minimumNumberOfCalls(5)
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(defaultConfig);
        registry.getEventPublisher()
                .onEntryAdded(event -> logger.info("Circuit breaker added: {}", event.getAddedEntry().getName()))
                .onEntryRemoved(event -> logger.info("Circuit breaker removed: {}", event.getRemovedEntry().getName()));

        return registry;
    }

    /**
     * Retry configuration for transient failures
     */
    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig defaultRetryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .retryOnException(throwable -> !(throwable instanceof IllegalArgumentException))
                .build();

        RetryRegistry registry = RetryRegistry.of(defaultRetryConfig);
        registry.getEventPublisher()
                .onEntryAdded(event -> logger.info("Retry registered: {}", event.getAddedEntry().getName()))
                .onEntryRemoved(event -> logger.info("Retry removed: {}", event.getRemovedEntry().getName()));

        return registry;
    }
}

