package abc.vaadin.views;

import abc.vaadin.components.CategoryForm;
import abc.vaadin.data.entity.Category;
import abc.vaadin.data.service.CategoryService;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Категории")
@Route(value = "category", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class CategoryView extends GridView<Category> {
    public CategoryView(CategoryService service, ProductService productService) {
        super(service, productService);
        resetDialog();
    }

    @Override
    protected CategoryForm createDialogProto() {
        return new CategoryForm();
    }

    @Override
    protected Grid<Category> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(Category::getName).setHeader("Название").setSortable(true);

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        return grid;
    }
}