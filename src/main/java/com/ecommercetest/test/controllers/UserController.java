package com.ecommercetest.test.controllers;

import com.ecommercetest.test.controllers.dto.edit_user.EditUserRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserResponse;
import com.ecommercetest.test.controllers.dto.sign_in.SignInRequest;
import com.ecommercetest.test.controllers.dto.sign_in.SignInResponse;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpRequest;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpResponse;
import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.controllers.response.CustomExceptionHandler;
import com.ecommercetest.test.domain.user.User;
import com.ecommercetest.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("auth/signup")
    @ResponseBody
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequest request) {
        try {
            request.validate();
        } catch (Exception e) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception);
        }
        try {
            SignUpResponse userResponse = userService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (DataIntegrityViolationException duplicateException) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.CONFLICT.value()),
                    duplicateException.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
        }
    }

    @PostMapping("auth/signin")
    @ResponseBody
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequest request) {
        try {
            request.validate();
        } catch (Exception e) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception);
        }
        try {
            final SignInResponse response = userService.signIn(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadCredentialsException e) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.UNAUTHORIZED.value()),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
        } catch (Exception e) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.BAD_REQUEST.value()),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }
    }

    @GetMapping("auth/oauth2/signin")
    @ResponseBody
    public ResponseEntity<ApiResponse> signinWithOauth2(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            try {
                String providerName = null;
                if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                    providerName = oauthToken.getAuthorizedClientRegistrationId();
                }
                if (providerName != null && !providerName.isEmpty()) {
                    if (providerName.equals("google")) {
                        SignInResponse response = userService.signInWithGoogle(authentication);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        SignInResponse response = userService.signInWithGithub(authentication);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    throw new OAuth2AuthenticationException("Invalid Oauth2 provider: " + providerName);
                }
            } catch (OAuth2AuthenticationException e) {
                CustomExceptionHandler exception = new CustomExceptionHandler(
                        CustomExceptionHandler.switchStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        e.getMessage());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception);
            } catch (Exception e) {
                CustomExceptionHandler exception = new CustomExceptionHandler(
                        CustomExceptionHandler.switchStatusCode(HttpStatus.UNAUTHORIZED.value()),
                        e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
            }
        } else {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.UNAUTHORIZED.value()),
                    "Full authentication is required. Please retry.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
        }
    }

    @PutMapping("users/edit")
    @ResponseBody
    public ResponseEntity<ApiResponse> editUser(@RequestBody EditUserRequest request) {
        try {
            request.validate();
            EditUserResponse response = userService.editUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception);
        } catch (Exception e) {
            CustomExceptionHandler exception = new CustomExceptionHandler(
                    CustomExceptionHandler.switchStatusCode(HttpStatus.BAD_REQUEST.value()),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }
    }
}