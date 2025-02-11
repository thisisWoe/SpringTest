package com.ecommercetest.test.config;

import com.ecommercetest.test.domain.role.Role;
import com.ecommercetest.test.domain.user.User;
import com.ecommercetest.test.domain.user.superadmin.SuperAdminProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class UserConfiguration {

    private final ObjectProvider<Role> adminRoleProvider;

    private final ObjectProvider<Role> userRoleProvider;

    private final ObjectProvider<PasswordEncoder> passwordEncoder;

    // Inietto la classe che mappa i valori "app.superadmin.*" dall'application-dev.yaml
    private final SuperAdminProperties superAdminProps;

    public UserConfiguration(@Qualifier("adminRole") ObjectProvider<Role> adminRoleProvider,
                             @Qualifier("userRole") ObjectProvider<Role> userRoleProvider,
                             @Qualifier("passwordEncoder") ObjectProvider<PasswordEncoder> passwordEncoder,
                             SuperAdminProperties superAdminProps) {
        this.adminRoleProvider = adminRoleProvider;
        this.userRoleProvider = userRoleProvider;
        this.passwordEncoder = passwordEncoder;
        this.superAdminProps = superAdminProps;
    }

    @Bean("superAdmin")
    @Scope("singleton")
    public User superAdmin() {
        final Set<Role> rolesSet = new HashSet<>();

        final Role admin = adminRoleProvider.getObject();
        final Role customer = userRoleProvider.getObject();

        Set<String> rolesFromProperties = superAdminProps.getRoles();

        if (rolesFromProperties.isEmpty()) {
            throw new IllegalArgumentException("Roles must not be empty");
        }

        rolesFromProperties.forEach(role -> {
            if (role.equals(admin.getName())) {
                rolesSet.add(admin);
            }
            if (role.equals(customer.getName())) {
                rolesSet.add(customer);
            }
        });

        return User.builder()
                .roles(rolesSet)
                .fullName(superAdminProps.getFullName())
                .username(superAdminProps.getUsername())
                .email(superAdminProps.getEmail())
                .password(passwordEncoder.getObject().encode(superAdminProps.getPassword()))
                .build();
    }
}
