package com.ecommercetest.test.controllers.exception;

import com.ecommercetest.test.controllers.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQUEST_ID_KEY = "requestId";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ErrorResponse.switchStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                ex.getMessage());
        log.error("[" + requestId + "]" + error.toString());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ErrorResponse.switchStatusCode(HttpStatus.CONFLICT.value()),
                ex.getMessage());
        log.error("[" + requestId + "]" + error.toString());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ErrorResponse.switchStatusCode(HttpStatus.UNAUTHORIZED.value()),
                ex.getMessage());
        log.error("[" + requestId + "]" + error.toString());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException ex) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ErrorResponse.switchStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                ex.getMessage());
        log.error("[" + requestId + "]" + error.toString());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorResponse.switchStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                ex.getMessage());
        log.error("[" + requestId + "]" + error.toString());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}