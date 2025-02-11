package com.ecommercetest.test.controllers;

import com.ecommercetest.test.controllers.dto.edit_user.ChangePasswordRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserResponse;
import com.ecommercetest.test.controllers.dto.edit_user.InsertMailRequest;
import com.ecommercetest.test.controllers.dto.sign_in.SignInRequest;
import com.ecommercetest.test.controllers.dto.sign_in.SignInResponse;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpRequest;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpResponse;
import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.domain.value_object.EmailValueObject;
import com.ecommercetest.test.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("auth/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequest request) {
        request.validate();
        SignUpResponse userResponse = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("auth/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequest request) {
        request.validate();
        SignInResponse response = userService.signIn(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("auth/oauth2/signin")
    public ResponseEntity<ApiResponse> signinWithOauth2(@RequestAttribute("org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken") Object authToken) {
        if (authToken instanceof OAuth2AuthenticationToken oauthToken) {
            String providerName = oauthToken.getAuthorizedClientRegistrationId();
            if (providerName != null && !providerName.isEmpty()) {
                SignInResponse response;
                if ("google".equals(providerName)) {
                    response = userService.signInWithGoogle(oauthToken);
                } else {
                    response = userService.signInWithGithub(oauthToken);
                }
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                throw new OAuth2AuthenticationException("Invalid OAuth2 provider: " + providerName);
            }
        } else {
            throw new BadCredentialsException("Full authentication is required. Please retry.");
        }
    }

    @PutMapping("users/edit")
    public ResponseEntity<ApiResponse> editUser(@RequestBody EditUserRequest request) {
        request.validate();
        EditUserResponse response = userService.editUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("users/insert-email/{id}")
    public ResponseEntity<ApiResponse> insertEmail(@PathVariable Long id, @RequestBody InsertMailRequest dto) {
        EmailValueObject emailObj = new EmailValueObject(dto.getEmail());
        EditUserResponse response = userService.insertEmail(emailObj, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("users/change-password/{id}")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        request.validate();
        EditUserResponse response = userService.changePassword(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}