package abc.vaadin.data.service;

import abc.vaadin.data.entity.Color;
import abc.vaadin.data.repository.ColorRepository;
import org.springframework.stereotype.Service;

@Service
public class ColorService extends AbstractService<Color> {
    protected ColorService(ColorRepository repository) {
        super(repository);
    }
}