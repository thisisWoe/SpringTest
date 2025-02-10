package com.ecommercetest.test.config;

import com.ecommercetest.test.domain.role.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RoleConfiguration {

    @Bean("adminRole")
    @Scope("singleton")
    public Role adminRole() {
        return Role.builder().id(1L).name("ADMIN").build();
    }

    @Bean("userRole")
    @Scope("singleton")
    public Role userRole() {
        return Role.builder().id(2L).name("CUSTOMER").build();
    }
}
