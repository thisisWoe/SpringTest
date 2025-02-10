package com.ecommercetest.test.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public interface ApplicationStartupService {

    @Transactional
    void startApplication();

    void writeProjectStructure() throws IOException;
}
