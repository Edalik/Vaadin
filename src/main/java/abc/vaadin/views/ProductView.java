package abc.vaadin.views;

import abc.vaadin.components.ProductForm;
import abc.vaadin.data.entity.*;
import abc.vaadin.data.repository.CartRepository;
import abc.vaadin.data.repository.UserRepository;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.security.SecurityService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Товары")
@PermitAll
public class ProductView extends VerticalLayout {
    Grid<Product> grid = new Grid<>(Product.class);
    private Filters filters;
    Button addToCart = new Button("В корзину");
    ProductForm productForm;
    ProductService productService;
    SecurityService securityService;
    UserRepository userRepository;
    CartRepository cartRepository;

    public ProductView(ProductService productService, SecurityService securityService, UserRepository userRepository, CartRepository cartRepository) {
        this.productService = productService;
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;

        setSizeFull();

        filters = new Filters(this::refreshGrid, productService);
        configureGrid();
        configureForm();

        add(filters, getToolbar(), getContent());
        closeEditor();
    }

    public static class Filters extends Div implements Specification<Product> {

        private final TextField brand = new TextField("Бренд");
        private final IntegerField priceFloor = new IntegerField("Цена от...");
        private final IntegerField priceCeiling = new IntegerField("Цена до...");
        private final MultiSelectComboBox<Color> colors = new MultiSelectComboBox<>("Цвета");
        private final MultiSelectComboBox<Category> categories = new MultiSelectComboBox<>("Категории");
        private final CheckboxGroup<Status> statuses = new CheckboxGroup<>("Статус");

        public Filters(Runnable onSearch, ProductService productService) {

            setWidthFull();
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            colors.setItems(productService.findAllColors(""));
            colors.setItemLabelGenerator(Color::getName);

            categories.setItems(productService.findAllCategories(""));
            categories.setItemLabelGenerator(Category::getName);

            statuses.setItems(productService.findAllStatuses(""));
            statuses.setItemLabelGenerator(Status::getName);
            statuses.addClassName("double-width");

            // Action buttons
            Button resetBtn = new Button("Сбросить");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                brand.clear();
                priceFloor.clear();
                priceCeiling.clear();
                colors.clear();
                categories.clear();
                statuses.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Применить");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            HorizontalLayout actions = new HorizontalLayout(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");
            actions.setWidthFull();
            actions.setAlignItems(Alignment.BASELINE);

            HorizontalLayout filters = new HorizontalLayout(brand, priceFloor, priceCeiling, colors, categories, statuses);
            filters.setWidthFull();
            filters.setAlignItems(Alignment.BASELINE);

            add(filters, actions);
        }

        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!brand.isEmpty()) {
                String lowerCaseFilter = brand.getValue().toLowerCase();
                Predicate brandMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")),
                        lowerCaseFilter + "%");
                predicates.add(brandMatch);
            }
            if (!priceFloor.isEmpty() && !priceCeiling.isEmpty()) {
                String databaseColumn = "price";

                Predicate priceMatch = criteriaBuilder.between(root.get(databaseColumn),
                        priceFloor.getValue(), priceCeiling.getValue());
                predicates.add(priceMatch);

            }
            else if (!priceFloor.isEmpty()) {
                String databaseColumn = "price";

                Predicate priceMatch = criteriaBuilder.greaterThan(root.get(databaseColumn),
                        priceFloor.getValue());
                predicates.add(priceMatch);

            }
            else if (!priceCeiling.isEmpty()) {
                String databaseColumn = "price";

                Predicate priceMatch = criteriaBuilder.lessThan(root.get(databaseColumn),
                        priceCeiling.getValue());
                predicates.add(priceMatch);

            }
            if (!colors.isEmpty()) {
                String databaseColumn = "color";
                List<Predicate> colorPredicates = new ArrayList<>();
                for (Color color : colors.getValue()) {
                    colorPredicates
                            .add(criteriaBuilder.equal(criteriaBuilder.literal(color), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(colorPredicates.toArray(Predicate[]::new)));
            }
            if (!categories.isEmpty()) {
                String databaseColumn = "category";
                List<Predicate> categoryPredicates = new ArrayList<>();
                for (Category category : categories.getValue()) {
                    categoryPredicates
                            .add(criteriaBuilder.equal(criteriaBuilder.literal(category), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(categoryPredicates.toArray(Predicate[]::new)));
            }
            if (!statuses.isEmpty()) {
                String databaseColumn = "status";
                List<Predicate> statusPredicates = new ArrayList<>();
                for (Status status : statuses.getValue()) {
                    statusPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(status), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(statusPredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, productForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, productForm);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        productForm = new ProductForm(productService.findAllColors(""), productService.findAllCategories(""), productService.findAllStatuses(""));
        productForm.addSaveListener(this::saveProduct);
        productForm.addDeleteListener(this::deleteProduct);
        productForm.addCloseListener(e -> closeEditor());
    }

    private void saveProduct(ProductForm.SaveEvent event) {
        productService.saveProduct(event.getProduct());
        refreshGrid();
        closeEditor();
    }

    private void deleteProduct(ProductForm.DeleteEvent event) {
        productService.deleteProduct(event.getProduct());
        refreshGrid();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("brand", "model", "price");
        grid.getColumns().get(0).setHeader("Бренд");
        grid.getColumns().get(1).setHeader("Модель");
        grid.getColumns().get(2).setHeader("Цена");
        grid.addColumn(product -> product.getColor().getName()).setHeader("Цвет").setSortable(true);
        grid.addColumn(product -> product.getCategory().getName()).setHeader("Категория").setSortable(true);
        grid.addColumn(product -> product.getStatus().getName()).setHeader("Статус").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(query -> productService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                filters).stream());
        grid.asSingleSelect().addValueChangeListener(event -> addToCart.setEnabled(event.getValue() != null &&
                event.getValue().getStatus().getName().equals("В наличии") &&
                cartRepository.getByIDs(event.getValue().getId(), userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()) == null));
        if (securityService.getAuthenticatedUser().getAuthorities().toString().contains(Role.ADMIN.toString())) {
            grid.asSingleSelect().addValueChangeListener(event -> editProduct(event.getValue()));
        }
    }

    private HorizontalLayout getToolbar() {

        Button addProductButton = new Button("Добавить товар");
        addProductButton.addClickListener(click -> addProduct());

        addToCart.setEnabled(false);
        addToCart.addClickListener(click -> addToCart(grid.asSingleSelect().getValue().getId(), userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));

        HorizontalLayout toolbar;

        if (securityService.getAuthenticatedUser().getAuthorities().toString().contains(Role.ADMIN.toString())) {
            toolbar = new HorizontalLayout(addToCart, addProductButton);
        } else {
            toolbar = new HorizontalLayout(addToCart);
        }
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void addToCart(Integer product_id, Integer user_id) {
        cartRepository.save(new Cart(product_id, user_id));
        addToCart.setEnabled(false);
    }

    private void closeEditor() {
        productForm.setProduct(null);
        productForm.setVisible(false);
    }

    private void editProduct(Product product) {
        if (product == null) {
            closeEditor();
        } else {
            productForm.setProduct(product);
            productForm.setVisible(true);
        }
    }

    private void addProduct() {
        grid.asSingleSelect().clear();
        editProduct(new Product());
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
