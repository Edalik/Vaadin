package abc.vaadin.views;

import abc.vaadin.components.ProviderForm;
import abc.vaadin.data.entity.Provider;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "provider", layout = MainLayout.class)
@PageTitle("Поставщики")
@RolesAllowed("ADMIN")
public class ProviderView extends VerticalLayout {
    Grid<Provider> grid = new Grid<>(Provider.class);
    TextField filterText = new TextField();
    Button editProvider = new Button("Изменить поставщика");
    Provider selectedProvider = new Provider();
    ProviderForm providerForm;
    ProductService productService;
    Dialog dialog = new Dialog();

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
        HorizontalLayout content = new HorizontalLayout(grid);
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
        grid.asSingleSelect().addValueChangeListener(event -> selectedProvider = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editProvider.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addProviderButton = new Button("Добавить поставщика");
        addProviderButton.addClickListener(click -> addProvider());

        editProvider.setEnabled(false);
        editProvider.addClickListener(e -> editProvider(selectedProvider, "Изменение поставщика"));

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addProviderButton, editProvider);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        providerForm.setProvider(null);
        providerForm.setVisible(false);
        dialog.close();
    }

    private void editProvider(Provider provider, String string) {
        if (provider == null) {
            closeEditor();
        } else {
            if (provider.getNumber() != null && provider.getNumber().length() > 10)
                provider.setNumber(provider.getNumber().substring(2));
            providerForm.setProvider(provider);
            providerForm.setVisible(true);
            dialog.add(providerForm);
            dialog.setHeaderTitle(string);
            dialog.open();
        }
    }

    private void addProvider() {
        grid.asSingleSelect().clear();
        editProvider(new Provider(), "Добавление поставщика");
    }

    private void updateList() {
        grid.setItems(productService.findAllProviders(filterText.getValue()));
    }
}
