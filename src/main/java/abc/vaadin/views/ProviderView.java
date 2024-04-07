package abc.vaadin.views;

import abc.vaadin.components.filters.ProductFilters;
import abc.vaadin.components.ProviderForm;
import abc.vaadin.data.entity.Provider;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.data.service.ProviderService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Поставщики")
@Route(value = "provider", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ProviderView extends GridView<Provider> {
    public ProviderView(ProviderService service, ProductService productService) {
        super(service, productService);
        resetDialog();
    }

    @Override
    protected ProviderForm createDialogProto() {
        return new ProviderForm(productService.findAllCities(""));
    }

    @Override
    protected ProductFilters createFiltersProto() {
        return null;
    }

    @Override
    protected Grid<Provider> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(Provider::getName).setHeader("Название").setSortable(true);
        grid.addColumn(provider -> provider.getCity().getName()).setHeader("Город").setSortable(true);
        grid.addColumn(Provider::getEmail).setHeader("Почта").setSortable(true);
        grid.addColumn(Provider::getNumber).setHeader("Номер").setSortable(true);

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        return grid;
    }
}