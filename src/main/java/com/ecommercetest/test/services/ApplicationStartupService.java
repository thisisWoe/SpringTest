package com.ecommercetest.test.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface ApplicationStartupService {

    @Transactional
    void startApplication();
}
