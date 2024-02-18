package abc.vaadin.views;

import abc.vaadin.data.entity.User;
import abc.vaadin.data.service.UserService;
import abc.vaadin.security.SecurityService;
import abc.vaadin.views.layout.MainLayout;
import abc.vaadin.views.registration.PasswordValidator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Профиль")
@PermitAll
public class ProfileView extends VerticalLayout {
    Binder<User> binder = new Binder<>(User.class);
    TextField surname = new TextField("Фамилия");
    TextField name = new TextField("Имя");
    TextField patronymic = new TextField("Отчество");
    TextField avatar = new TextField("Аватар");
    Button save = new Button("Сохранить изменения");
    Button changeLogin = new Button("Изменить логин");
    Button changePassword = new Button("Изменить пароль");
    UserService userService;
    SecurityService securityService;

    public ProfileView(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;

        binder.bindInstanceFields(this);
        binder.setBean(userService.findByLogin(securityService.getAuthenticatedUser().getUsername()));

        add(createFieldsLayout(), createAvatarLayout(), createButtonsLayout());
    }

    private HorizontalLayout createAvatarLayout() {
        Image image = new Image();
        image.setSrc(binder.getBean().getAvatar());
        image.setHeight("80px");
        image.setWidth("80px");

        avatar.setWidth("750px");
        avatar.addValueChangeListener(e -> image.setSrc(avatar.getValue()));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(image, avatar);
        horizontalLayout.setAlignItems(Alignment.END);

        return horizontalLayout;
    }

    private HorizontalLayout createFieldsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(surname, name, patronymic);
        horizontalLayout.setAlignItems(Alignment.BASELINE);

        return horizontalLayout;
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> validateAndSave());

        changeLogin.addClickListener(e -> createChangeLoginDialog());
        changePassword.addClickListener(e -> createChangePasswordDialog());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(save, changeLogin, changePassword);
        horizontalLayout.setAlignItems(Alignment.BASELINE);

        return horizontalLayout;
    }

    private void createChangeLoginDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Изменение логина");
        VerticalLayout dialogLayout = new VerticalLayout();
        TextField login = new TextField("Введите новый логин");
        login.setRequired(true);
        PasswordField password = new PasswordField("Текущий пароль");
        password.setRequired(true);

        Button change = new Button("Изменить");
        change.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        change.addClickListener(e -> changeLoginEvent(login.getValue(), password.getValue(), dialog, binder.getBean()));

        Button cancel = new Button("Отменить");
        cancel.addClickListener(e -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(change, cancel);
        dialogLayout.add(login, password, buttonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void changeLoginEvent(String login, String password, Dialog dialog, User user) {
        if (login.isEmpty()) {
            Notification notification = new Notification("Логин не введен");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
            notification.setDuration(1000);
            return;
        }
        if (!userService.isLoginAvailable(login)) {
            Notification notification = new Notification("Логин занят");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
            notification.setDuration(1000);
            return;
        }
        if (isPasswordIncorrect(password, user)) return;
        user.setLogin(login);
        user.setPassword(password);
        userService.saveUser(user);
        dialog.close();
    }

    private void createChangePasswordDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Изменение пароля");
        VerticalLayout dialogLayout = new VerticalLayout();
        PasswordField password = new PasswordField("Введите текущий пароль");
        PasswordField passwordNew = new PasswordField("Введите новый пароль");
        PasswordField passwordNewConfirm = new PasswordField("Подтвердите новый пароль");

        passwordNewConfirm.addValueChangeListener(e -> binder.validate());
        passwordNewConfirm.setRequired(true);

        Button change = new Button("Изменить");
        change.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        change.addClickListener(e -> changePasswordEvent(password.getValue(), passwordNew.getValue(), dialog, binder.getBean()));

        Button cancel = new Button("Отменить");
        cancel.addClickListener(e -> dialog.close());

        binder.forField(passwordNew)
                .asRequired("Поле должно быть заполнено")
                .withValidator(new PasswordValidator(passwordNew, passwordNewConfirm))
                .bind(User::getPassword, User::setPassword);
        passwordNew.clear();
        binder.addStatusChangeListener(e -> change.setEnabled(binder.isValid()));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(change, cancel);
        dialogLayout.add(password, passwordNew, passwordNewConfirm, buttonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void changePasswordEvent(String password, String passwordNew, Dialog dialog, User user) {
        if (isPasswordIncorrect(password, user)) return;
        user.setPassword(passwordNew);
        userService.saveUser(user);
        dialog.close();
    }

    private boolean isPasswordIncorrect(String password, User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())){
            Notification notification = new Notification("Неверный пароль");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
            notification.setDuration(1000);
            return true;
        }
        return false;
    }

    private void validateAndSave() {
        if (binder.isValid()){
            userService.saveUser(binder.getBean());
            UI.getCurrent().getPage().reload();
        }
    }
}
