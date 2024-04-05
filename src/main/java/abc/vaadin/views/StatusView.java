package abc.vaadin.views;

import abc.vaadin.components.StatusForm;
import abc.vaadin.data.entity.Status;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.data.service.StatusService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Статусы")
@Route(value = "status", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class StatusView extends GridView<Status> {
    public StatusView(StatusService service, ProductService productService) {
        super(service, productService);
        resetDialog();
    }

    @Override
    protected StatusForm createDialogProto() {
        return new StatusForm();
    }

    @Override
    protected Grid<Status> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(Status::getName).setHeader("Название");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        return grid;
    }
}