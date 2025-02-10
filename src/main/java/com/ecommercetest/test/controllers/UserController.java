package com.ecommercetest.test.controllers;

import com.ecommercetest.test.controllers.dto.edit_user.EditUserRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserResponse;
import com.ecommercetest.test.controllers.dto.sign_in.SignInRequest;
import com.ecommercetest.test.controllers.dto.sign_in.SignInResponse;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpRequest;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpResponse;
import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

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
}