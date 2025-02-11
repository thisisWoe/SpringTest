package com.ecommercetest.test.domain.value_object;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class EmailValueObject {

    private final String email;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w+_.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    public EmailValueObject(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("The email cannot be null or empty.");
        }
        email = email.trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.email = email;
    }
}
