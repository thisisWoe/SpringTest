package com.ecommercetest.test.controllers.dto.edit_user;

import lombok.Getter;

@Getter
public class InsertMailRequest {
    private final String email;

    public InsertMailRequest(String email) {
        this.email = email;
    }
}
