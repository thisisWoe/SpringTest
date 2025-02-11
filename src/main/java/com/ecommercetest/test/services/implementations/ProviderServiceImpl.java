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
    private final ObjectProvider<Provider> googleProvider;

    private final ObjectProvider<Provider> githubProvider;

    private final ProviderRepository providerRepository;

    public ProviderServiceImpl(@Qualifier("googleProvider") ObjectProvider<Provider> googleProvider,
                               @Qualifier("githubProvider") ObjectProvider<Provider> githubProvider,
                               ProviderRepository providerRepository) {
        this.googleProvider = googleProvider;
        this.githubProvider = githubProvider;
        this.providerRepository = providerRepository;
    }


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
