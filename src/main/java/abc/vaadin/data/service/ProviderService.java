package abc.vaadin.data.service;

import abc.vaadin.data.entity.Provider;
import abc.vaadin.data.repository.ProviderRepository;
import org.springframework.stereotype.Service;

@Service
public class ProviderService extends AbstractService<Provider> {
    protected ProviderService(ProviderRepository repository) {
        super(repository);
    }
}