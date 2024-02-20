package abc.vaadin.views;

import abc.vaadin.components.CategoryForm;
import abc.vaadin.data.entity.Category;
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

@Route(value = "category", layout = MainLayout.class)
@PageTitle("Категории")
@RolesAllowed("ADMIN")
public class CategoryView extends VerticalLayout {
    Grid<Category> grid = new Grid<>(Category.class);
    TextField filterText = new TextField();
    Button editCategory = new Button("Изменить категорию");
    Category selectedCategory = new Category();
    CategoryForm categoryForm;
    ProductService productService;
    SecurityService securityService;
    Dialog dialog = new Dialog();

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
        HorizontalLayout content = new HorizontalLayout(grid);
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
        grid.asSingleSelect().addValueChangeListener(event -> selectedCategory = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editCategory.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по категории");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCategoryButton = new Button("Добавить категорию");
        addCategoryButton.addClickListener(click -> addCategory());

        editCategory.setEnabled(false);
        editCategory.addClickListener(e -> editCategory(selectedCategory, "Изменение категории"));

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addCategoryButton, editCategory);

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        categoryForm.setCategory(null);
        categoryForm.setVisible(false);
        dialog.close();
    }

    private void editCategory(Category category, String string) {
        if (category == null) {
            closeEditor();
        } else {
            categoryForm.setCategory(category);
            categoryForm.setVisible(true);
            dialog.add(categoryForm);
            dialog.setHeaderTitle(string);
            dialog.open();
        }
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Category(), "Добавление категории");
    }

    private void updateList() {
        grid.setItems(productService.findAllCategories(filterText.getValue()));
    }
}
