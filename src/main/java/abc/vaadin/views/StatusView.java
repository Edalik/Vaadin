package abc.vaadin.views;

import abc.vaadin.components.StatusForm;
import abc.vaadin.data.entity.Status;
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
import jakarta.annotation.security.RolesAllowed;

@Route(value = "status", layout = MainLayout.class)
@PageTitle("Статусы")
@RolesAllowed("ADMIN")
public class StatusView extends VerticalLayout {
    Grid<Status> grid = new Grid<>(Status.class);
    TextField filterText = new TextField();
    Button editStatus = new Button("Изменить статус");
    Status selectedStatus = new Status();
    StatusForm statusForm;
    ProductService productService;
    SecurityService securityService;
    Dialog dialog = new Dialog();

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
        HorizontalLayout content = new HorizontalLayout(grid);
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
        grid.asSingleSelect().addValueChangeListener(event -> selectedStatus = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editStatus.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по статусу");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addStatusButton = new Button("Добавить статус");
        addStatusButton.addClickListener(click -> addStatus());

        editStatus.setEnabled(false);
        editStatus.addClickListener(e -> editStatus(selectedStatus, "Изменение статуса"));

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addStatusButton, editStatus);
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        statusForm.setStatus(null);
        statusForm.setVisible(false);
        dialog.close();
    }

    private void editStatus(Status status, String string) {
        if (status == null) {
            closeEditor();
        } else {
            statusForm.setStatus(status);
            statusForm.setVisible(true);
            dialog.add(statusForm);
            dialog.setHeaderTitle(string);
            dialog.open();
        }
    }

    private void addStatus() {
        grid.asSingleSelect().clear();
        editStatus(new Status(), "Добавление статуса");
    }

    private void updateList() {
        grid.setItems(productService.findAllStatuses(filterText.getValue()));
    }
}
