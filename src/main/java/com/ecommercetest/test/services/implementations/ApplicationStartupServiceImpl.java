package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.services.ApplicationStartupService;
import com.ecommercetest.test.services.ProviderService;
import com.ecommercetest.test.services.RoleService;
import com.ecommercetest.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationStartupServiceImpl implements ApplicationStartupService {
    @Autowired
    ProviderService providerService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public void startApplication() {
        try {
            providerService.checkProvidersPresentOrSave();
            roleService.checkRolesPresentOrSave();
            userService.checkAdminPresentOrSave();
        } catch (Exception e) {
            System.out.println("Something went wrong while trying the application startup\n: " + e);
        }
    }
}
