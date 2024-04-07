package abc.vaadin.components.filters;

import abc.vaadin.data.entity.*;
import abc.vaadin.data.service.ProductService;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ProductFilters extends Filters<Product> {
    private final ProductService productService;
    private final TextField brand;
    private final TextField model;
    private final IntegerField priceFloor;
    private final IntegerField priceCeiling;
    private final MultiSelectComboBox<Color> colors;
    private final MultiSelectComboBox<Category> categories;
    private final MultiSelectComboBox<Provider> providers;
    private final CheckboxGroup<Status> statuses;

    public ProductFilters(Runnable onSearch, ProductService productService) {
        super(onSearch);

        this.brand = createBrandField();
        this.model = createModelField();
        this.priceFloor = createPriceFloorField();
        this.priceCeiling = createPriceCeilingField();
        this.productService = productService;
        this.colors = createColorsBox();
        this.categories = createCategoriesBox();
        this.providers = createProvidersBox();
        this.statuses = createStatusesBox();

        resetBtn.addClickListener(e -> {
            brand.clear();
            model.clear();
            priceFloor.clear();
            priceCeiling.clear();
            colors.clear();
            categories.clear();
            statuses.clear();
            providers.clear();
        });

        addToFilters(brand, model, priceFloor, priceCeiling, colors, categories, providers, statuses);
    }

    private TextField createBrandField() {
        var field = new TextField("Бренд");
        field.setClearButtonVisible(true);

        return field;
    }

    private TextField createModelField() {
        var field = new TextField("Модель");
        field.setClearButtonVisible(true);

        return field;
    }

    private IntegerField createPriceFloorField() {
        var field = new IntegerField("Цена от...");
        field.setClearButtonVisible(true);

        return field;
    }

    private IntegerField createPriceCeilingField() {
        var field = new IntegerField("Цена до...");
        field.setClearButtonVisible(true);

        return field;
    }

    private MultiSelectComboBox<Color> createColorsBox() {
        var box = new MultiSelectComboBox<Color>("Цвета");
        box.setItems(productService.findAllColors(""));
        box.setItemLabelGenerator(Color::getName);
        box.setClearButtonVisible(true);

        return box;
    }

    private MultiSelectComboBox<Category> createCategoriesBox() {
        var box = new MultiSelectComboBox<Category>("Категории");
        box.setItems(productService.findAllCategories(""));
        box.setItemLabelGenerator(Category::getName);
        box.setClearButtonVisible(true);

        return box;
    }

    private MultiSelectComboBox<Provider> createProvidersBox() {
        var box = new MultiSelectComboBox<Provider>("Поставщик");
        box.setItems(productService.findAllProviders(""));
        box.setItemLabelGenerator(Provider::toString);
        box.setClearButtonVisible(true);

        return box;
    }

    private CheckboxGroup<Status> createStatusesBox() {
        var box = new CheckboxGroup<Status>("Статус");
        box.setItems(productService.findAllStatuses(""));
        box.setItemLabelGenerator(Status::getName);
        box.addClassName("double-width");

        return box;
    }

    @Override
    public Predicate toPredicate(Root<Product> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (!brand.isEmpty()) {
            String lowerCaseFilter = brand.getValue().toLowerCase();
            Predicate brandMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")),
                    lowerCaseFilter + "%");
            predicates.add(brandMatch);
        }
        if (!model.isEmpty()) {
            String lowerCaseFilter = model.getValue().toLowerCase();
            Predicate brandMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("model")),
                    lowerCaseFilter + "%");
            predicates.add(brandMatch);
        }
        if (!priceFloor.isEmpty() && !priceCeiling.isEmpty()) {
            String databaseColumn = "price";

            Predicate priceMatch = criteriaBuilder.between(root.get(databaseColumn),
                    priceFloor.getValue(), priceCeiling.getValue());
            predicates.add(priceMatch);

        } else if (!priceFloor.isEmpty()) {
            String databaseColumn = "price";

            Predicate priceMatch = criteriaBuilder.greaterThan(root.get(databaseColumn),
                    priceFloor.getValue());
            predicates.add(priceMatch);

        } else if (!priceCeiling.isEmpty()) {
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
        if (!providers.isEmpty()) {
            String databaseColumn = "provider";
            List<Predicate> providerPredicates = new ArrayList<>();
            for (Provider provider : providers.getValue()) {
                providerPredicates
                        .add(criteriaBuilder.equal(criteriaBuilder.literal(provider), root.get(databaseColumn)));
            }
            predicates.add(criteriaBuilder.or(providerPredicates.toArray(Predicate[]::new)));
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