package com.ecommercetest.test.domain.user.superadmin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.superadmin")
public class SuperAdminProperties {

    private String fullName;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;

}
