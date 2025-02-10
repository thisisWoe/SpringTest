package com.ecommercetest.test.security.model.logger;

import com.ecommercetest.test.controllers.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID_KEY, requestId);

        long startTime = System.currentTimeMillis();

        log.info("[{}] Incoming Request: {} {} - Params: {}", requestId,
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        log.info("[{}] Outgoing Response: {} {} - Status: {} - Time: {} ms", requestId,
                request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        log.info("[{}] Status code explanation: {}", requestId,
                ErrorResponse.switchStatusCode(response.getStatus()));

        MDC.remove(REQUEST_ID_KEY);
    }
}
