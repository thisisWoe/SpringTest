package com.ecommercetest.test.config;

import com.ecommercetest.test.domain.provider.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ProviderConfiguration {

    @Bean("googleProvider")
    @Scope("singleton")
    public Provider googleProvider() {
        return Provider.builder().id(1L).name("GOOGLE").build();
    }

    @Bean("githubProvider")
    @Scope("singleton")
    public Provider githubProvider() {
        return Provider.builder().id(2L).name("GITHUB").build();
    }
}
