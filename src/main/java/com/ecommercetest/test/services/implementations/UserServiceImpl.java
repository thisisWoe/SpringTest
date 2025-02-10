package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.TestApplication;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserRequest;
import com.ecommercetest.test.controllers.dto.edit_user.EditUserResponse;
import com.ecommercetest.test.controllers.dto.sign_in.SignInRequest;
import com.ecommercetest.test.controllers.dto.sign_in.SignInResponse;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpRequest;
import com.ecommercetest.test.controllers.dto.sign_up.SignUpResponse;
import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.controllers.response.UserMinimized;
import com.ecommercetest.test.domain.provider.Provider;
import com.ecommercetest.test.domain.role.Role;
import com.ecommercetest.test.domain.user.User;
import com.ecommercetest.test.domain.user.superadmin.SuperAdminProperties;
import com.ecommercetest.test.environment.ActiveProfiles;
import com.ecommercetest.test.repositories.ProviderRepository;
import com.ecommercetest.test.repositories.RoleRepository;
import com.ecommercetest.test.repositories.UserRepository;
import com.ecommercetest.test.runner.ApplicationRunner;
import com.ecommercetest.test.security.model.GithubUserInfo;
import com.ecommercetest.test.security.model.GoogleUserInfo;
import com.ecommercetest.test.security.model.SecurityUser;
import com.ecommercetest.test.security.model.jwt.JwtAuthenticationFilter;
import com.ecommercetest.test.security.model.jwt.JwtTokenProvider;
import com.ecommercetest.test.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.ecommercetest.test.security.service.OAuth2UserService;
import com.ecommercetest.test.security.service.SecurityUserService;
import com.ecommercetest.test.services.ApplicationStartupService;
import com.ecommercetest.test.services.ProviderService;
import com.ecommercetest.test.services.RoleService;
import com.ecommercetest.test.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("superAdmin")
    private ObjectProvider<User> superAdminProvider;

    @Autowired
    @Qualifier("userRole")
    private ObjectProvider<Role> customerRole;

    @Autowired
    @Qualifier("googleProvider")
    private ObjectProvider<Provider> googleProvider;

    @Autowired
    @Qualifier("githubProvider")
    private ObjectProvider<Provider> githubProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void checkAdminPresentOrSave() {
        User superAdmin = superAdminProvider.getObject();

        boolean superAdminAlreadyExists = userRepository.findByUsername(superAdmin.getUsername()).isPresent();

        if (!superAdminAlreadyExists) {
            userRepository.saveAndFlush(superAdmin);
        }
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already in use.");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole.getObject());

        User user = User.builder()
                .roles(roles)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        final User result = userRepository.save(user);

        return SignUpResponse.builder()
                .id(result.getId())
                .username(result.getUsername())
                .build();
    }

    @Override
    public SignInResponse signIn(SignInRequest request) {
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new EntityNotFoundException("Username not found.");
        }

        User userFound = userRepository.findByUsername(request.getUsername()).orElseThrow();

        // Creo il token di autenticazione con username e password
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword());

        // Passo il token all'AuthenticationManager per eseguire l'autenticazione
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Se l'autenticazione ha successo, il token Authentication risulterà
        // "authenticated"
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // qui posso generare un token jwt, e preparare tutto il resto delle operazioni
        String accessToken = jwtProvider.generateAccessToken(authentication);

        return new SignInResponse(
                userFound.getId(),
                userFound.getFullName(),
                userFound.getUsername(),
                userFound.getEmail(),
                userFound.getProvider(),
                accessToken);
    }

    @Override
    public SignInResponse signInWithGoogle(Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();
        GoogleUserInfo googleUser;
        try {
            googleUser = objectMapper.convertValue(attributes, GoogleUserInfo.class);
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("Impossible to map GoogleUserInfo attributes.");
        }
        User userFound;
        if (!userRepository.existsByUsername(googleUser.getEmail())
                && !userRepository.existsByEmail(googleUser.getEmail())) {
            Set<Role> roles = new HashSet<>();
            roles.add(customerRole.getObject());

            User user = User.builder()
                    .roles(roles)
                    .username(googleUser.getEmail())
                    .email(googleUser.getEmail())
                    .provider(googleProvider.getObject())
                    .fullName(googleUser.getName())
                    .build();
            userFound = userRepository.saveAndFlush(user);
        } else {
            userFound = userRepository.findByUsername(googleUser.getEmail())
                    .orElseGet(() -> userRepository.findByEmail(googleUser.getEmail())
                            .orElseThrow(() -> new RuntimeException("User not found")));
            if (!userFound.getProvider().getId().equals(googleProvider.getObject().getId())) {
                throw new BadCredentialsException(
                        "User can't sign in with Google if it signed up with username and password.");
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtProvider.generateAccessToken(authentication);

        return new SignInResponse(
                userFound.getId(),
                userFound.getFullName(),
                userFound.getUsername(),
                userFound.getEmail(),
                userFound.getProvider(),
                accessToken);
    }

    @Override
    public SignInResponse signInWithGithub(Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();
        GithubUserInfo githubUser;
        try {
            githubUser = objectMapper.convertValue(attributes, GithubUserInfo.class);
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("Impossible to map GithubUserInfo attributes.");
        }

        User userFound;
        if (!userRepository.existsByUsername(githubUser.getLogin()) &&
                !userRepository.existsByUsername(githubUser.getEmail()) &&
                !userRepository.existsByEmail(githubUser.getEmail())) {

            Set<Role> roles = new HashSet<>();
            roles.add(customerRole.getObject());

            User user = User.builder()
                    .roles(roles)
                    .username(githubUser.getEmail() != null ? githubUser.getEmail() : githubUser.getLogin())
                    .email(githubUser.getEmail())
                    .provider(githubProvider.getObject())
                    .fullName(githubUser.getName())
                    .build();
            userFound = userRepository.saveAndFlush(user);
        } else {
            userFound = userRepository.findByUsername(githubUser.getEmail())
                    .orElseGet(() -> userRepository.findByEmail(githubUser.getEmail())
                            .orElseThrow(() -> new RuntimeException("User not found")));

            if (!userFound.getProvider().getId().equals(githubProvider.getObject().getId())) {
                throw new BadCredentialsException(
                        "User can't sign in with GitHub if they signed up with a different method.");
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtProvider.generateAccessToken(authentication);

        return new SignInResponse(
                userFound.getId(),
                userFound.getFullName(),
                userFound.getUsername(),
                userFound.getEmail(),
                userFound.getProvider(),
                accessToken);
    }

    @Override
    public EditUserResponse editUser(EditUserRequest request) {
        if (!userRepository.existsById(request.getId())) {
            throw new EntityNotFoundException("User not found.");
        }
        User userFound = userRepository.findById(request.getId()).get();
        User newUser = User.builder()
                .id(request.getId())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .username(userFound.getUsername())
                .password(userFound.getPassword())
                .roles(userFound.getRoles())
                .provider(userFound.getProvider())
                .build();

        User savedUser = userRepository.save(newUser);
        return EditUserResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .provider(savedUser.getProvider())
                .build();
    }
}
