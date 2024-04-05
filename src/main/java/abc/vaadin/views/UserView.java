package abc.vaadin.views;

import abc.vaadin.components.UserForm;
import abc.vaadin.data.entity.User;
import abc.vaadin.data.service.ProductService;
import abc.vaadin.data.service.UserService;
import abc.vaadin.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Пользователи")
@Route(value = "user", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class UserView extends GridView<User> {
    public UserView(UserService service, ProductService productService) {
        super(service, productService);
        resetDialog();
    }

    @Override
    protected UserForm createDialogProto() {
        return new UserForm();
    }

    @Override
    protected Grid<User> createGrid() {
        var grid = super.createGrid();

        grid.addColumn(User::getName).setHeader("Имя").setSortable(true);
        grid.addColumn(User::getSurname).setHeader("Фамилия").setSortable(true);
        grid.addColumn(User::getPatronymic).setHeader("Отчество").setSortable(true);
        grid.addColumn(User::getAvatar).setHeader("Аватар").setSortable(true);
        grid.addColumn(User::getLogin).setHeader("Логин").setSortable(true);
        grid.addColumn(User::getPassword).setHeader("Пароль").setSortable(true);
        grid.addColumn(User::getRole).setHeader("Роль").setSortable(true);

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        return grid;
    }
}