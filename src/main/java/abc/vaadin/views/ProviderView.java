package abc.vaadin.views;

import abc.vaadin.components.ProviderForm;
import abc.vaadin.data.entity.Provider;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "provider", layout = MainLayout.class)
@PageTitle("Поставщики")
@PermitAll
public class ProviderView extends VerticalLayout {
    Grid<Provider> grid = new Grid<>(Provider.class);
    TextField filterText = new TextField();
    ProviderForm providerForm;
    ProductService productService;

    public ProviderView(ProductService productService) {
        this.productService = productService;

        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, providerForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, providerForm);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        providerForm = new ProviderForm(productService.findAllCities(""));
        providerForm.addSaveListener(this::saveProvider);
        providerForm.addDeleteListener(this::deleteProvider);
        providerForm.addCloseListener(e -> closeEditor());
    }

    private void saveProvider(ProviderForm.SaveEvent event) {
        productService.saveProvider(event.getProvider());
        updateList();
        closeEditor();
    }

    private void deleteProvider(ProviderForm.DeleteEvent event) {
        productService.deleteProvider(event.getProvider());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name", "email", "number");
        grid.getColumns().get(0).setHeader("Название");
        grid.getColumns().get(1).setHeader("Почта");
        grid.getColumns().get(2).setHeader("Телефон");
        grid.addColumn(provider -> provider.getCity().getName()).setHeader("Город").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editProvider(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addProviderButton = new Button("Добавить поставщика");
        addProviderButton.addClickListener(click -> addProvider());

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addProviderButton);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        providerForm.setProvider(null);
        providerForm.setVisible(false);
    }

    private void editProvider(Provider provider) {
        if (provider == null) {
            closeEditor();
        } else {
            if (provider.getNumber().length() > 10)
                provider.setNumber(provider.getNumber().substring(2));
            providerForm.setProvider(provider);
            providerForm.setVisible(true);
        }
    }

    private void addProvider() {
        grid.asSingleSelect().clear();
        editProvider(new Provider());
    }

    private void updateList() {
        grid.setItems(productService.findAllProviders(filterText.getValue()));
    }
}
