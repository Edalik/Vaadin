package abc.vaadin.data.service;

import abc.vaadin.data.entity.Category;
import abc.vaadin.data.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends AbstractService<Category> {
    protected CategoryService(CategoryRepository repository) {
        super(repository);
    }
}
