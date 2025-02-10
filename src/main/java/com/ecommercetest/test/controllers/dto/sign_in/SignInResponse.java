package com.ecommercetest.test.controllers.dto.sign_in;

import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.domain.provider.Provider;
import lombok.Getter;

@Getter
public class SignInResponse implements ApiResponse {

    private final Long id;
    private final String fullName;
    private final String username;
    private final String email;
    private final Provider provider;
    private final String accessToken;

    public SignInResponse(Long id, String fullName, String username, String email, Provider provider, String accessToken) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.accessToken = accessToken;
    }

}
