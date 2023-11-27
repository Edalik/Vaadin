package abc.vaadin.views;

import abc.vaadin.data.entity.Product;
import abc.vaadin.data.repository.CartRepository;
import abc.vaadin.data.repository.UserRepository;
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

@Route(value = "cart", layout = MainLayout.class)
@PageTitle("Корзина")
@PermitAll
public class CartView extends VerticalLayout {
    Grid<Product> grid = new Grid<>(Product.class);
    TextField filterText = new TextField();
    Button removeFromCart = new Button("Удалить из корзины");
    ProductService productService;
    SecurityService securityService;
    CartRepository cartRepository;
    UserRepository userRepository;

    public CartView(ProductService productService, SecurityService securityService, CartRepository cartRepository, UserRepository userRepository) {
        this.productService = productService;
        this.securityService = securityService;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;

        setSizeFull();

        configureGrid();

        add(getToolbar(), getContent());
        updateList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("brand", "model", "price");
        grid.getColumns().get(0).setHeader("Бренд");
        grid.getColumns().get(1).setHeader("Модель");
        grid.getColumns().get(2).setHeader("Цена");
        grid.addColumn(product -> product.getColor().getName()).setHeader("Цвет");
        grid.addColumn(product -> product.getCategory().getName()).setHeader("Категория");
        grid.addColumn(product -> product.getStatus().getName()).setHeader("Статус");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> removeFromCart.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Сортировать по бренду...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar;

        removeFromCart.setEnabled(false);
        removeFromCart.addClickListener(click -> removeFromCart(grid.asSingleSelect().getValue().getId(), userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));

        toolbar = new HorizontalLayout(filterText, removeFromCart);
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void removeFromCart(Integer product_id, Integer user_id) {
        cartRepository.delete(cartRepository.getByIDs(product_id, user_id));
        removeFromCart.setEnabled(false);
        updateList();
    }

    private void updateList() {
        grid.setItems(cartRepository.findByUserID(userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername()).getId()));
    }
}
