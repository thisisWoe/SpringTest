package com.ecommercetest.test.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate.limit")
@Getter
@Setter
public class RateLimitProperties {

    /**
     * Numero massimo di richieste consentite
     */
    private int requests;

    /**
     * Durata in minuti per il refill del bucket
     */
    private int durationMinutes;
}