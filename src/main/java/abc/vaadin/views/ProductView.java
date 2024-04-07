package abc.vaadin.views;

import abc.vaadin.components.ProductFilters;
import abc.vaadin.components.ProductEditForm;
import abc.vaadin.data.entity.Cart;
import abc.vaadin.data.entity.Product;
import abc.vaadin.data.entity.Role;
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

@PageTitle("Товары")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class ProductView extends GridView<Product> {
    SecurityService securityService;
    UserService userService;
    private final Button addToCart;
    public ProductView(ProductService productService,
                       UserService userService,
                       SecurityService securityService) {
        super(productService, productService);
        resetDialog();
        this.userService = userService;
        this.securityService = securityService;

        this.addToCart = createAddToCartButton();
        setFilterTextVisibility(false);

        if (!securityService.getAuthenticatedUser().getAuthorities().toString().contains(Role.ADMIN.toString())){
            setAddButtonVisibility(false);
            setEditButtonVisibility(false);
        }

        addToToolBar(addToCart);
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

        return new ProductFilters(this::refreshGrid, productService);
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

    private void addToCart(Long product_id, Long user_id) {
        productService.saveCart(new Cart(product_id, user_id));
        addToCart.setEnabled(false);
    }

    private Button createAddToCartButton(){
        var button = new Button("В корзину");

        grid.asSingleSelect().addValueChangeListener(event -> button.setEnabled(event.getValue() != null &&
                event.getValue().getStatus().getName().equals("В наличии") &&
                productService.getByIDs(event.getValue().getId(), userService.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()) == null));

        button.setEnabled(false);
        button.addClickListener(click -> addToCart(grid.asSingleSelect().getValue().getId(),
                userService.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));
        return button;
    }
}