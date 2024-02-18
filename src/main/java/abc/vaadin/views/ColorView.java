package abc.vaadin.views;

import abc.vaadin.components.ColorForm;
import abc.vaadin.data.entity.Color;
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

@Route(value = "color", layout = MainLayout.class)
@PageTitle("Цвета")
@PermitAll
public class ColorView extends VerticalLayout {
    Grid<Color> grid = new Grid<>(Color.class);
    TextField filterText = new TextField();
    ColorForm colorForm;
    ProductService productService;
    SecurityService securityService;

    public ColorView(ProductService productService, SecurityService securityService) {
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
        HorizontalLayout content = new HorizontalLayout(grid, colorForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, colorForm);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        colorForm = new ColorForm();
        colorForm.addSaveListener(this::saveColor);
        colorForm.addDeleteListener(this::deleteColor);
        colorForm.addCloseListener(e -> closeEditor());
    }

    private void saveColor(ColorForm.SaveEvent event) {
        productService.saveColor(event.getColor());
        updateList();
        closeEditor();
    }

    private void deleteColor(ColorForm.DeleteEvent event) {
        productService.deleteColor(event.getColor());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().get(0).setHeader("Цвет");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editColor(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по цвету");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addColorButton = new Button("Добавить цвет");
        addColorButton.addClickListener(click -> addColor());

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addColorButton);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        colorForm.setColor(null);
        colorForm.setVisible(false);
    }

    private void editColor(Color color) {
        if (color == null) {
            closeEditor();
        } else {
            colorForm.setColor(color);
            colorForm.setVisible(true);
        }
    }

    private void addColor() {
        grid.asSingleSelect().clear();
        editColor(new Color());
    }

    private void updateList() {
        grid.setItems(productService.findAllColors(filterText.getValue()));
    }
}