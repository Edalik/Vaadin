package abc.vaadin.data.service;

import abc.vaadin.data.entity.Status;
import abc.vaadin.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService extends AbstractService<Status> {
    protected StatusService(StatusRepository repository) {
        super(repository);
    }
}