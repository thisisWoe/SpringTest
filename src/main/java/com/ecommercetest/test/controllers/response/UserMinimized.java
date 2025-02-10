package com.ecommercetest.test.controllers.response;

import com.ecommercetest.test.domain.provider.Provider;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserMinimized {

    private final Long id;
    private final String fullName;
    private final String username;
    private final String email;
    private final Provider provider;

    public UserMinimized(Long id, String fullName, String username, String email, Provider provider) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.provider = provider;
    }

}
