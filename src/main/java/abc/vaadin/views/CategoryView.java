package abc.vaadin.views;

import abc.vaadin.components.CategoryForm;
import abc.vaadin.data.entity.Category;
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

@Route(value = "category", layout = MainLayout.class)
@PageTitle("Категории")
@PermitAll
public class CategoryView extends VerticalLayout {
    Grid<Category> grid = new Grid<>(Category.class);
    TextField filterText = new TextField();
    CategoryForm categoryForm;
    ProductService productService;
    SecurityService securityService;

    public CategoryView(ProductService productService, SecurityService securityService) {
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
        HorizontalLayout content = new HorizontalLayout(grid, categoryForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, categoryForm);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        categoryForm = new CategoryForm();
        categoryForm.addSaveListener(this::saveCategory);
        categoryForm.addDeleteListener(this::deleteCategory);
        categoryForm.addCloseListener(e -> closeEditor());
    }

    private void saveCategory(CategoryForm.SaveEvent event) {
        productService.saveCategory(event.getCategory());
        updateList();
        closeEditor();
    }

    private void deleteCategory(CategoryForm.DeleteEvent event) {
        productService.deleteCategory(event.getCategory());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().get(0).setHeader("Категория");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editCategory(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Сортировать...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCategoryButton = new Button("Добавить категорию");
        addCategoryButton.addClickListener(click -> addCategory());

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addCategoryButton);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        categoryForm.setCategory(null);
        categoryForm.setVisible(false);
    }

    private void editCategory(Category category) {
        if (category == null) {
            closeEditor();
        } else {
            categoryForm.setCategory(category);
            categoryForm.setVisible(true);
        }
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Category());
    }

    private void updateList() {
        grid.setItems(productService.findAllCategories(filterText.getValue()));
    }
}
