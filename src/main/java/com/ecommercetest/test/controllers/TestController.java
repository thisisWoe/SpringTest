package com.ecommercetest.test.controllers;

import com.ecommercetest.test.controllers.response.ApiResponse;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/try")
    @ResponseBody
    public ResponseEntity<ApiResponse> signUp() {
        final Prova prova = new Prova(
                "Titolo di prova",
                "Descrizione di prova"
        );
        return ResponseEntity.ok(prova);
    }

    @GetMapping("/resp")
    @ResponseBody
    public ResponseEntity<ApiResponse> loginOauth2(Authentication authentication) {
        final Prova prova = new Prova(
                "Titolo di prova",
                "Descrizione di prova"
        );
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> userAttributes = oauthUser.getAttributes();
//            return ResponseEntity.ok(userAttributes);
        }
        return ResponseEntity.ok(prova);
    }

}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Prova implements ApiResponse {
    private String title;
    private String description;
}