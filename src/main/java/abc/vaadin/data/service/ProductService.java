package abc.vaadin.data.service;

import abc.vaadin.data.entity.Category;
import abc.vaadin.data.entity.Product;
import abc.vaadin.data.entity.Status;
import abc.vaadin.data.repository.CategoryRepository;
import abc.vaadin.data.repository.ProductRepository;
import abc.vaadin.data.repository.StatusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, StatusRepository statusRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.statusRepository = statusRepository;
    }

    public Page<Product> list(Pageable pageable, Specification<Product> filter) {
        return productRepository.findAll(filter, pageable);
    }

    public List<Product> findAllProducts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return productRepository.findAll();
        } else {
            return productRepository.search(stringFilter);
        }
    }

    public long countProducts() {
        return productRepository.count();
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public void saveProduct(Product product) {
        if (product == null) {
            return;
        }
        productRepository.save(product);
    }

    public void saveCategory(Category category) {
        if (category == null) {
            return;
        }
        categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void saveStatus(Status status) {
        if (status == null) {
            return;
        }
        statusRepository.save(status);
    }

    public void deleteStatus(Status status) {
        statusRepository.delete(status);
    }

    public List<Category> findAllCategories(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.search(stringFilter);
        }
    }

    public List<Status> findAllStatuses(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return statusRepository.findAll();
        } else {
            return statusRepository.search(stringFilter);
        }
    }
}
