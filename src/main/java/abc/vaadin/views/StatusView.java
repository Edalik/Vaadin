package abc.vaadin.views;

import abc.vaadin.components.StatusForm;
import abc.vaadin.data.entity.Status;
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

@Route(value = "status", layout = MainLayout.class)
@PageTitle("Статусы")
@PermitAll
public class StatusView extends VerticalLayout {
    Grid<Status> grid = new Grid<>(Status.class);
    TextField filterText = new TextField();
    StatusForm statusForm;
    ProductService productService;
    SecurityService securityService;

    public StatusView(ProductService productService, SecurityService securityService) {
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
        HorizontalLayout content = new HorizontalLayout(grid, statusForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, statusForm);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        statusForm = new StatusForm();
        statusForm.addSaveListener(this::saveStatus);
        statusForm.addDeleteListener(this::deleteStatus);
        statusForm.addCloseListener(e -> closeEditor());
    }

    private void saveStatus(StatusForm.SaveEvent event) {
        productService.saveStatus(event.getStatus());
        updateList();
        closeEditor();
    }

    private void deleteStatus(StatusForm.DeleteEvent event) {
        productService.deleteStatus(event.getStatus());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().get(0).setHeader("Статус");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editStatus(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Сортировать...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addStatusButton = new Button("Добавить статус");
        addStatusButton.addClickListener(click -> addStatus());

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addStatusButton);
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        statusForm.setStatus(null);
        statusForm.setVisible(false);
    }

    private void editStatus(Status status) {
        if (status == null) {
            closeEditor();
        } else {
            statusForm.setStatus(status);
            statusForm.setVisible(true);
        }
    }

    private void addStatus() {
        grid.asSingleSelect().clear();
        editStatus(new Status());
    }

    private void updateList() {
        grid.setItems(productService.findAllStatuses(filterText.getValue()));
    }
}
