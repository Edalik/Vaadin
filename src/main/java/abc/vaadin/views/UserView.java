package abc.vaadin.views;

import abc.vaadin.components.UserForm;
import abc.vaadin.data.entity.User;
import abc.vaadin.data.service.UserService;
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
import jakarta.annotation.security.RolesAllowed;

@Route(value = "user", layout = MainLayout.class)
@PageTitle("Пользователи")
@RolesAllowed("ADMIN")
public class UserView extends VerticalLayout {
    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    Button editUser = new Button("Изменить пользователя");
    User selectedUser = new User();
    UserForm userForm;
    UserService userService;
    SecurityService securityService;
    Dialog dialog = new Dialog();

    public UserView(UserService userService, SecurityService securityService) {
        this.userService = userService;
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
        userForm = new UserForm();
        userForm.addSaveListener(this::saveUser);
        userForm.addDeleteListener(this::deleteUser);
        userForm.addCloseListener(e -> closeEditor());
    }

    private void saveUser(UserForm.SaveEvent event) {
        userService.saveUser(event.getUser());
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userService.deleteUser(event.getUser());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("surname", "name", "patronymic", "avatar", "login", "password", "role");
        grid.getColumns().get(0).setHeader("Фамилия");
        grid.getColumns().get(1).setHeader("Имя");
        grid.getColumns().get(2).setHeader("Отчество");
        grid.getColumns().get(3).setHeader("Аватар");
        grid.getColumns().get(4).setHeader("Логин");
        grid.getColumns().get(5).setHeader("Пароль");
        grid.getColumns().get(6).setHeader("Роль");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectedUser = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editUser.setEnabled(event.getValue() != null));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по логину");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addUserButton = new Button("Добавить пользователя");
        addUserButton.addClickListener(click -> addUser());

        editUser.setEnabled(false);
        editUser.addClickListener(e -> editUser(selectedUser, "Изменение статуса"));

        HorizontalLayout toolbar;

        toolbar = new HorizontalLayout(filterText, addUserButton, editUser);
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        return toolbar;
    }

    private void closeEditor() {
        userForm.setUser(null);
        userForm.setVisible(false);
        dialog.close();
    }

    private void editUser(User user, String string) {
        if (user == null) {
            closeEditor();
        } else {
            userForm.setUser(user);
            userForm.setVisible(true);
            dialog.add(userForm);
            dialog.setHeaderTitle(string);
            dialog.open();
        }
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User(), "Добавление пользователя");
    }

    private void updateList() {
        grid.setItems(userService.findAllUsers(filterText.getValue()));
    }
}