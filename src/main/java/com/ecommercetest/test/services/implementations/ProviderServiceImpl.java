package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.domain.provider.Provider;
import com.ecommercetest.test.repositories.ProviderRepository;
import com.ecommercetest.test.services.ProviderService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    @Qualifier("googleProvider")
    private ObjectProvider<Provider> googleProvider;

    @Autowired
    @Qualifier("githubProvider")
    private ObjectProvider<Provider> githubProvider;

    @Autowired
    private ProviderRepository providerRepository;


    @Override
    public void checkProvidersPresentOrSave() {
        Provider google = googleProvider.getObject();
        Provider github = githubProvider.getObject();

        boolean googleAlreadyExists = providerRepository.existsById(google.getId());
        boolean githubAlreadyExists = providerRepository.existsById(github.getId());

        if (!googleAlreadyExists) {
            providerRepository.saveAndFlush(google);
        }

        if (!githubAlreadyExists) {
            providerRepository.saveAndFlush(github);
        }
    }
}
