package abc.vaadin.views;

import abc.vaadin.components.CityForm;
import abc.vaadin.data.entity.City;
import abc.vaadin.data.service.CityService;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Города")
@Route(value = "city", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class CityView extends GridView<City> {
    public CityView(CityService service, ProductService productService) {
        super(service, productService);
        resetDialog();
    }

    @Override
    protected CityForm createDialogProto() {
        return new CityForm();
    }

    @Override
    protected Grid<City> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(City::getName).setHeader("Название").setSortable(true);

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        return grid;
    }
}