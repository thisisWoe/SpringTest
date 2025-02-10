package com.ecommercetest.test.controllers.dto.sign_up;

import com.ecommercetest.test.environment.ActiveProfiles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = false)
public class SignUpRequest {

    private String username;
    private String password;

    public SignUpRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void validate() {
        if (username == null) {
            throw new IllegalArgumentException("Username is mandatory.");
        }
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username can't be empty.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password is mandatory.");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password can't be empty.");
        }
        if (password.length() < 8 && (ActiveProfiles.isProd() || ActiveProfiles.isTest())) {
            throw new IllegalArgumentException("The password must be at least 8 characters long.");
        }

        if (!isValidPassword(password) && (ActiveProfiles.isProd() || ActiveProfiles.isTest())) {
            throw new IllegalArgumentException(
                    "Password must contain at least one special character, one digit, one uppercase letter, and one lowercase letter."
            );
        }
    }

    /**
     * Validates the password against the required rules:
     * - At least one digit.
     * - At least one lowercase letter.
     * - At least one uppercase letter.
     * - At least one special character.
     */
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}
