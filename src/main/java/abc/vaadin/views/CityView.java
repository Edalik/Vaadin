package abc.vaadin.views;

import abc.vaadin.components.CityForm;
import abc.vaadin.components.CityForm;
import abc.vaadin.data.entity.City;
import abc.vaadin.data.entity.City;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.security.SecurityService;
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

@Route(value = "city", layout = MainLayout.class)
@PageTitle("Города")
@PermitAll
public class CityView extends VerticalLayout {
    Grid<City> grid = new Grid<>(City.class);
    TextField filterText = new TextField();
    CityForm cityForm;
    ProductService productService;
    SecurityService securityService;

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
        HorizontalLayout content = new HorizontalLayout(grid, cityForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, cityForm);
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
        grid.asSingleSelect().addValueChangeListener(event ->
                editCity(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCityButton = new Button("Добавить город");
        addCityButton.addClickListener(click -> addCity());

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addCityButton);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        cityForm.setCity(null);
        cityForm.setVisible(false);
    }

    private void editCity(City city) {
        if (city == null) {
            closeEditor();
        } else {
            cityForm.setCity(city);
            cityForm.setVisible(true);
        }
    }

    private void addCity() {
        grid.asSingleSelect().clear();
        editCity(new City());
    }

    private void updateList() {
        grid.setItems(productService.findAllCities(filterText.getValue()));
    }
}
