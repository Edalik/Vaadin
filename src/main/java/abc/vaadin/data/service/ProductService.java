package abc.vaadin.data.service;

import abc.vaadin.data.entity.*;
import abc.vaadin.data.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final CartRepository cartRepository;
    private final CityRepository cityRepository;
    private final ProviderRepository providerRepository;

    public ProductService(ProductRepository productRepository,
                          ColorRepository colorRepository,
                          CategoryRepository categoryRepository,
                          StatusRepository statusRepository,
                          CartRepository cartRepository,
                          CityRepository cityRepository,
                          ProviderRepository providerRepository) {
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.categoryRepository = categoryRepository;
        this.statusRepository = statusRepository;
        this.cartRepository = cartRepository;
        this.cityRepository = cityRepository;
        this.providerRepository = providerRepository;
    }

    public Page<Product> list(Pageable pageable,
                              Specification<Product> filter) {
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

    public void saveColor(Color color) {
        if (color == null) {
            return;
        }
        colorRepository.save(color);
    }

    public void deleteColor(Color color) {
        colorRepository.delete(color);
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

    public List<Color> findAllColors(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return colorRepository.findAll();
        } else {
            return colorRepository.search(stringFilter);
        }
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

    public Cart getByIDs(Integer productId, Integer userId) {
        return cartRepository.getByIDs(productId, userId);
    }

    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public List<Product> findByUserID(Integer user_id) {
        return cartRepository.findByUserID(user_id);
    }

    public void saveCity(City city) {
        if (city == null) {
            return;
        }
        cityRepository.save(city);
    }

    public void deleteCity(City city) {
        cityRepository.delete(city);
    }

    public List<City> findAllCities(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return cityRepository.findAll();
        } else {
            return cityRepository.search(stringFilter);
        }
    }

    public void saveProvider(Provider provider) {
        if (provider == null) {
            return;
        }
        providerRepository.save(provider);
    }

    public void deleteProvider(Provider provider) {
        providerRepository.delete(provider);
    }

    public List<Provider> findAllProviders(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return providerRepository.findAll();
        } else {
            return providerRepository.search(stringFilter);
        }
    }
}
