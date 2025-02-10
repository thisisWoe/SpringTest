package com.ecommercetest.test.runner;

import com.ecommercetest.test.services.ApplicationStartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private ApplicationStartupService applicationStartupService;

    @Override
    public void run(String... args) throws Exception {
        applicationStartupService.startApplication();
    }
}
