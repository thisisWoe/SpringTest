package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.domain.role.Role;
import com.ecommercetest.test.repositories.RoleRepository;
import com.ecommercetest.test.services.RoleService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    @Qualifier("adminRole")
    private ObjectProvider<Role> adminRoleProvider;

    @Autowired
    @Qualifier("userRole")
    private ObjectProvider<Role> userRoleProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void checkRolesPresentOrSave() {
        Role admin = adminRoleProvider.getObject();
        Role user = userRoleProvider.getObject();

        boolean adminAlreadyExists = roleRepository.existsById(admin.getId());
        boolean userAlreadyExists = roleRepository.existsById(user.getId());

        if (!adminAlreadyExists) {
            roleRepository.saveAndFlush(admin);
        }

        if (!userAlreadyExists) {
            roleRepository.saveAndFlush(user);
        }
    }
}
