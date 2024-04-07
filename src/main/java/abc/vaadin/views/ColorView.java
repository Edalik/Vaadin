package abc.vaadin.views;

import abc.vaadin.components.ColorForm;
import abc.vaadin.components.filters.ProductFilters;
import abc.vaadin.data.entity.Color;
import abc.vaadin.data.service.ColorService;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Цвета")
@Route(value = "color", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ColorView extends GridView<Color> {
    public ColorView(ColorService service, ProductService productService) {
        super(service, productService);
        resetDialog();
    }

    @Override
    protected ColorForm createDialogProto() {
        return new ColorForm();
    }

    @Override
    protected ProductFilters createFiltersProto() {
        return null;
    }

    @Override
    protected Grid<Color> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(Color::getName).setHeader("Название").setSortable(true);

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        return grid;
    }
}