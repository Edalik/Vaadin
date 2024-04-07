package abc.vaadin.views;

import abc.vaadin.components.filters.ProductFilters;
import abc.vaadin.components.ProductEditForm;
import abc.vaadin.data.entity.Product;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.data.service.UserService;
import abc.vaadin.security.SecurityService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;

@Route(value = "cart", layout = MainLayout.class)
@PageTitle("Корзина")
@PermitAll
public class CartView extends GridView<Product> {
    SecurityService securityService;
    UserService userService;
    private final Button removeFromCart;

    public CartView(ProductService productService,
                        UserService userService,
                        SecurityService securityService) {
        super(productService, productService);
        resetDialog();
        this.userService = userService;
        this.securityService = securityService;
        this.removeFromCart = createRemoveFromCartButton();
        setAddButtonVisibility(false);
        setEditButtonVisibility(false);
        addToToolBar(removeFromCart);
        grid.setItems(productService.findByUserID(userService.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));
    }

    @Override
    protected ProductEditForm createDialogProto() {
        return new ProductEditForm(productService.findAllColors(""),
                productService.findAllCategories(""),
                productService.findAllStatuses(""),
                productService.findAllProviders(""));
    }

    @Override
    protected ProductFilters createFiltersProto() {
        return null;
    }

    @Override
    protected Grid<Product> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(Product::getBrand).setHeader("Бренд").setSortable(true);
        grid.addColumn(Product::getModel).setHeader("Модель").setSortable(true);
        grid.addColumn(Product::getPrice).setHeader("Цена").setSortable(true);
        grid.addColumn(product -> product.getColor().getName()).setHeader("Цвет").setSortable(true);
        grid.addColumn(product -> product.getCategory().getName()).setHeader("Категория").setSortable(true);
        grid.addColumn(product -> product.getStatus().getName()).setHeader("Статус").setSortable(true);
        grid.addColumn(product -> product.getProvider().getName()).setHeader("Поставщик").setSortable(true);
        grid.addColumn(product -> product.getProvider().getEmail()).setHeader("Почта поставщика").setSortable(true);
        grid.addColumn(product -> product.getProvider().getNumber()).setHeader(" Телефон поставщика").setSortable(true);
        grid.addColumn(product -> product.getProvider().getCity().getName()).setHeader("Город поставщика").setSortable(true);

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        grid.setItems(query -> productService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                productFilters).stream());

        return grid;
    }

    @Override
    protected void refreshGrid() {
        grid.setItems(productService.findByUserID(userService.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));
    }

    private Button createRemoveFromCartButton() {
        var button = new Button("Удалить из корзины");

        grid.asSingleSelect().addValueChangeListener(event -> removeFromCart.setEnabled(event.getValue() != null));

        button.setEnabled(false);
        button.addClickListener(click -> removeFromCart(grid.asSingleSelect().getValue().getId(),
                userService.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));

        return button;
    }

    private void removeFromCart(Long product_id, Long user_id) {
        productService.deleteCart(productService.getByIDs(product_id, user_id));
        removeFromCart.setEnabled(false);
        refreshGrid();
    }
}