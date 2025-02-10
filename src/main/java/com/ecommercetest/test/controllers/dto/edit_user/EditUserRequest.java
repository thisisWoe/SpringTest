package com.ecommercetest.test.controllers.dto.edit_user;

import lombok.Getter;

@Getter
public class EditUserRequest {
    private Long id;
    private String fullName;
    private String email;

    public void validate() {
        if (id == null) {
            throw new IllegalArgumentException("User ID is mandatory.");
        }

        boolean isFullNameEmpty = (fullName == null || fullName.trim().isEmpty());
        boolean isEmailEmpty = (email == null || email.trim().isEmpty());

        if (isFullNameEmpty && isEmailEmpty) {
            throw new IllegalArgumentException("At least one field (full name or email) must be provided.");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!email.matches(emailRegex)){
            throw new IllegalArgumentException("Email must be a valid email address.");
        }
    }
}
