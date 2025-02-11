package com.ecommercetest.test.services;

import com.ecommercetest.test.controllers.dto.edit_user.ChangePasswordRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserResponse;
import com.ecommercetest.test.controllers.dto.sign_in.SignInRequest;
import com.ecommercetest.test.controllers.dto.sign_in.SignInResponse;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpRequest;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpResponse;

import com.ecommercetest.test.domain.value_object.EmailValueObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void checkAdminPresentOrSave();

    SignUpResponse signUp(SignUpRequest request);

    SignInResponse signIn(SignInRequest request);

    SignInResponse signInWithGoogle(Authentication authentication);

    SignInResponse signInWithGithub(Authentication authentication);

    EditUserResponse editUser(EditUserRequest request);

    EditUserResponse insertEmail(EmailValueObject email, Long id);

    EditUserResponse changePassword(ChangePasswordRequest request, Long id);
}
