package abc.vaadin.views;

import abc.vaadin.components.CityForm;
import abc.vaadin.data.entity.City;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.security.SecurityService;
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
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "city", layout = MainLayout.class)
@PageTitle("Города")
@RolesAllowed("ADMIN")
public class CityView extends VerticalLayout {
    Grid<City> grid = new Grid<>(City.class);
    TextField filterText = new TextField();
    Button editCity = new Button("Изменить город");
    City selectedCity = new City();
    CityForm cityForm;
    ProductService productService;
    SecurityService securityService;
    Dialog dialog = new Dialog();

    public CityView(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;

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
        cityForm = new CityForm();
        cityForm.addSaveListener(this::saveCity);
        cityForm.addDeleteListener(this::deleteCity);
        cityForm.addCloseListener(e -> closeEditor());
    }

    private void saveCity(CityForm.SaveEvent event) {
        productService.saveCity(event.getCity());
        updateList();
        closeEditor();
    }

    private void deleteCity(CityForm.DeleteEvent event) {
        productService.deleteCity(event.getCity());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().get(0).setHeader("Город");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectedCity = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editCity.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCityButton = new Button("Добавить город");
        addCityButton.addClickListener(click -> addCity());

        editCity.setEnabled(false);
        editCity.addClickListener(e -> editCity(selectedCity, "Изменение города"));

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addCityButton, editCity);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        cityForm.setCity(null);
        cityForm.setVisible(false);
        dialog.close();
    }

    private void editCity(City city, String string) {
        if (city == null) {
            closeEditor();
        } else {
            cityForm.setCity(city);
            cityForm.setVisible(true);
            dialog.add(cityForm);
            dialog.setHeaderTitle(string);
            dialog.open();
        }
    }

    private void addCity() {
        grid.asSingleSelect().clear();
        editCity(new City(), "Добавление города");
    }

    private void updateList() {
        grid.setItems(productService.findAllCities(filterText.getValue()));
    }
}
