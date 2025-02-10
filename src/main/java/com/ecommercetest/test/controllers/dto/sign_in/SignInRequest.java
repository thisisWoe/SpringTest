package com.ecommercetest.test.controllers.dto.sign_in;


import com.ecommercetest.test.environment.ActiveProfiles;
import lombok.Getter;

@Getter
public class SignInRequest {

    private final String username;
    private final String password;

    public SignInRequest(String username, String password) {
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
    }
}
