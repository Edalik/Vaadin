package abc.vaadin.views;

import abc.vaadin.components.ColorForm;
import abc.vaadin.data.entity.Color;
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

@Route(value = "color", layout = MainLayout.class)
@PageTitle("Цвета")
@RolesAllowed("ADMIN")
public class ColorView extends VerticalLayout {
    Grid<Color> grid = new Grid<>(Color.class);
    TextField filterText = new TextField();
    Button editColor = new Button("Изменить цвет");
    Color selectedColor = new Color();
    ColorForm colorForm;
    ProductService productService;
    SecurityService securityService;
    Dialog dialog = new Dialog();

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
        HorizontalLayout content = new HorizontalLayout(grid);
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
        grid.asSingleSelect().addValueChangeListener(event -> selectedColor = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editColor.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по цвету");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addColorButton = new Button("Добавить цвет");
        addColorButton.addClickListener(click -> addColor());

        editColor.setEnabled(false);
        editColor.addClickListener(e -> editColor(selectedColor, "Изменение цвета"));

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addColorButton, editColor);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        colorForm.setColor(null);
        colorForm.setVisible(false);
        dialog.close();
    }

    private void editColor(Color color, String string) {
        if (color == null) {
            closeEditor();
        } else {
            colorForm.setColor(color);
            colorForm.setVisible(true);
            dialog.add(colorForm);
            dialog.setHeaderTitle(string);
            dialog.open();
        }
    }

    private void addColor() {
        grid.asSingleSelect().clear();
        editColor(new Color(), "Добавление цвета");
    }

    private void updateList() {
        grid.setItems(productService.findAllColors(filterText.getValue()));
    }
}