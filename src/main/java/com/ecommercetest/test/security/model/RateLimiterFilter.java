package com.ecommercetest.test.security.model;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    // Mappa per memorizzare i bucket per ogni IP (o chiave di identificazione)
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final RateLimitProperties rateLimitProperties;

    // Crea un nuovo bucket usando i parametri configurati
    private Bucket createNewBucket() {
        int requests = rateLimitProperties.getRequests();
        int durationMinutes = rateLimitProperties.getDurationMinutes();
        Refill refill = Refill.greedy(requests, Duration.ofMinutes(durationMinutes));
        Bandwidth limit = Bandwidth.classic(requests, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    public RateLimiterFilter(RateLimitProperties rateLimitProperties) {
        this.rateLimitProperties = rateLimitProperties;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for IP: {}", ip);
            response.setContentType("application/json");
            response.setStatus(429);
            response.getWriter().write("{\"message\": \"Too Many Requests. Please try again later.\"}");
            response.getWriter().flush();
        }
    }
}