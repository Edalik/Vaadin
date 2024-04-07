package abc.vaadin.components;

import abc.vaadin.data.entity.*;
import abc.vaadin.data.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductFilters extends Div implements Specification<Product> {
    private final TextField brand = new TextField("Бренд");
    private final TextField model = new TextField("Модель");
    private final IntegerField priceFloor = new IntegerField("Цена от...");
    private final IntegerField priceCeiling = new IntegerField("Цена до...");
    private final MultiSelectComboBox<Color> colors = new MultiSelectComboBox<>("Цвета");
    private final MultiSelectComboBox<Category> categories = new MultiSelectComboBox<>("Категории");
    private final MultiSelectComboBox<Provider> providers = new MultiSelectComboBox<>("Поставщик");
    private final CheckboxGroup<Status> statuses = new CheckboxGroup<>("Статус");
    public final Button resetBtn;
    public final Button searchBtn;

    public ProductFilters(Runnable onSearch, ProductService productService) {

        setWidthFull();

        colors.setItems(productService.findAllColors(""));
        colors.setItemLabelGenerator(Color::getName);

        categories.setItems(productService.findAllCategories(""));
        categories.setItemLabelGenerator(Category::getName);

        providers.setItems(productService.findAllProviders(""));
        providers.setItemLabelGenerator(Provider::toString);

        statuses.setItems(productService.findAllStatuses(""));
        statuses.setItemLabelGenerator(Status::getName);
        statuses.addClassName("double-width");

        // Action buttons
        resetBtn = new Button("Сбросить");
        resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetBtn.addClickListener(e -> {
            brand.clear();
            model.clear();
            priceFloor.clear();
            priceCeiling.clear();
            colors.clear();
            categories.clear();
            statuses.clear();
            providers.clear();
            onSearch.run();
        });
        searchBtn = new Button("Применить");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> onSearch.run());

        HorizontalLayout actions = new HorizontalLayout(resetBtn, searchBtn);
        actions.addClassName(LumoUtility.Gap.SMALL);
        actions.addClassName("actions");
        actions.setWidthFull();
        actions.setAlignItems(FlexComponent.Alignment.BASELINE);

        HorizontalLayout filters = new HorizontalLayout(brand, model, priceFloor, priceCeiling, colors, categories, providers, statuses);
        filters.setWidthFull();
        filters.setAlignItems(FlexComponent.Alignment.BASELINE);

        add(filters, actions);
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