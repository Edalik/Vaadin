package abc.vaadin.components.filters;

import abc.vaadin.data.entity.AbstractEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public abstract class Filters<TEntity extends AbstractEntity> extends Div implements Specification<TEntity> {
    protected final Button resetBtn;
    private final Button searchBtn;
    private final HorizontalLayout filters;

    public Filters(Runnable onSearch) {
        this.filters = createFilters();
        this.resetBtn = createResetButton();
        this.searchBtn = createSearchButton();

        setWidthFull();

        resetBtn.addClickListener(e -> {
            onSearch.run();
        });

        searchBtn.addClickListener(e -> onSearch.run());

        add(filters, createToolBar());
    }

    private HorizontalLayout createFilters() {
        var filters = new HorizontalLayout();
        filters.setWidthFull();
        filters.setAlignItems(FlexComponent.Alignment.BASELINE);

        return filters;
    }

    private Button createResetButton() {
        var button = new Button("Сбросить");
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        return button;
    }

    private Button createSearchButton() {
        var button = new Button("Применить");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return button;
    }

    private Component createToolBar() {
        HorizontalLayout actions = new HorizontalLayout(resetBtn, searchBtn);
        actions.addClassName(LumoUtility.Gap.SMALL);
        actions.addClassName("actions");
        actions.setWidthFull();
        actions.setAlignItems(FlexComponent.Alignment.BASELINE);

        return actions;
    }

    public void addToFilters(Component... components) {
        filters.add(components);
    }

    @Override
    public Predicate toPredicate(Root<TEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
