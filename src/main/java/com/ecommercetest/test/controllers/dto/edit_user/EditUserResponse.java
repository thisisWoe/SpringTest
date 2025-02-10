package com.ecommercetest.test.controllers.dto.edit_user;

import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.controllers.response.UserMinimized;
import com.ecommercetest.test.domain.provider.Provider;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EditUserResponse extends UserMinimized implements ApiResponse{

    public EditUserResponse(Long id, String fullName, String username, String email, Provider provider) {
        super(id, fullName, username, email, provider);
    }

}
