package com.ecommercetest.test.controllers.dto.edit_user;

import com.ecommercetest.test.environment.ActiveProfiles;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private final String oldPassword;
    private final String newPassword;
    private final String confirmNewPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public void validate() {
        if (oldPassword == null) {
            throw new IllegalArgumentException("Old password is mandatory.");
        }
        if (oldPassword.isEmpty()) {
            throw new IllegalArgumentException("Old password can't be empty.");
        }
        if (newPassword == null) {
            throw new IllegalArgumentException("New password is mandatory.");
        }
        if (newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password can't be empty.");
        }
        if (newPassword.length() < 8 && (ActiveProfiles.isProd() || ActiveProfiles.isTest())) {
            throw new IllegalArgumentException("New password must be at least 8 characters long.");
        }
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New password and confirm new password must match.");
        }
        if (!isValidPassword(newPassword) && (ActiveProfiles.isProd() || ActiveProfiles.isTest())) {
            throw new IllegalArgumentException(
                    "New password must contain at least one special character, one digit, one uppercase letter, and one lowercase letter."
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
