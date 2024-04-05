package abc.vaadin.data.service;

import abc.vaadin.data.entity.City;
import abc.vaadin.data.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AbstractService<City> {
    protected CityService(CityRepository repository) {
        super(repository);
    }
}