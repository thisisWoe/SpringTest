package com.ecommercetest.test.runner;

import com.ecommercetest.test.services.ApplicationStartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private final ApplicationStartupService applicationStartupService;

    public ApplicationRunner(ApplicationStartupService applicationStartupService) {
        this.applicationStartupService = applicationStartupService;
    }

    @Override
    public void run(String... args) throws Exception {
        applicationStartupService.startApplication();
        applicationStartupService.writeProjectStructure();
        applicationStartupService.createDevices();
    }

}
