package com.ecommercetest.test.security.config;

import com.ecommercetest.test.security.model.jwt.JwtAuthenticationFilter;
import com.ecommercetest.test.security.oauth2.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean("passwordEncoder")
    @Scope("singleton")
    /**
     * encoder delle password
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Espone l’AuthenticationManager come bean,
     * utilizzando la configurazione automatica di Spring Security.
     * <p>
     * Con Spring Security >= 6.1, è il modo raccomandato:
     * recupera l'AuthenticationManager da AuthenticationConfiguration.
     */
    @Bean("authenticationManager")
    @Scope("singleton")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Definisce la catena di filtri di Spring Security (SecurityFilterChain).
     */
    @Bean("securityFilterChain")
    @Scope("singleton")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disabilito CSRF se sto costruendo API REST.
                // Se invece uso form login classico e sessioni, posso valutare di abilitarlo.
                .csrf(AbstractHttpConfigurer::disable)

                // Autorizzazioni sulle rotte
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**", "/oauth2/**")
                        .permitAll()
                        .anyRequest().authenticated())
                // Configura il login OAuth2
                .oauth2Login(oauth2 -> oauth2
                        // Posso impostare una pagina di login personalizzata oppure usare quella di
                        // default
                        // .loginPage("/login") // pagina custom
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        // È possibile configurare eventuali handler per il successo o il fallimento del
                        // login OAuth2
                        .defaultSuccessUrl("/api/auth/oauth2/signin") // o qualsiasi endpoint dopo il login
                        .failureUrl("/login?error=true"))
                // (Opzionale) Form login di default
                // .formLogin(conf -> conf.);

                // È usato per controllare ogni richiesta che arriva, estraendo e
                // validando un token JWT (se presente) nell’header Authorization
                // È responsabile della creazione di un oggetto Authentication a partire dai
                // dati inviati,
                // che poi viene impostato nel SecurityContextHolder se la validazione ha
                // successo
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configurazione AuthenticationEntryPoint
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            String jsonResponse = String.format(
                                    "{\"statusCodeExplanation\": \"Authentication is required. Please log in and try again.\", \"message\": \"%s\"}",
                                    authException.getMessage());
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        }));
        // costruisco e restituisco la catena di sicurezza
        return http.build();
    }
}
