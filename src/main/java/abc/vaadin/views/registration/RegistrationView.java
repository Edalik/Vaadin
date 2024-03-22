package abc.vaadin.views.registration;

import abc.vaadin.data.entity.Role;
import abc.vaadin.data.entity.User;
import abc.vaadin.data.service.UserService;
import abc.vaadin.views.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("registration")
@PageTitle("Регистрация")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);
    private final TextField surname = new TextField("Фамилия");
    private final TextField name = new TextField("Имя");
    private final TextField patronymic = new TextField("Отчество");
    private final Image image = new Image();
    private final TextField avatar = new TextField("Аватар");
    private final TextField login = new TextField("Логин");
    private final PasswordField password = new PasswordField("Пароль");
    private final PasswordField passwordConfirm = new PasswordField("Подтвердите пароль");
    private final ComboBox<Role> role = new ComboBox<>();
    private final Button register = new Button("Зарегистрироваться", buttonClickEvent -> register());
    private final UserService userService;
    boolean isFinishingRegistration = false;

    public RegistrationView(UserService userService) {
        this.userService = userService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        configureForm();
        configureBinder();
        binder.bindInstanceFields(this);

        add(new H2("Регистрация"),
                surname,
                name,
                patronymic,
                createAvatarLayout(),
                login,
                password,
                passwordConfirm,
                role,
                register);
    }

    private void configureForm() {
        role.setLabel("Роль");
        role.setItems(Role.values());
        role.setValue(Role.USER);

        passwordConfirm.addValueChangeListener(e -> binder.validate());
        passwordConfirm.setRequired(true);
        surname.setVisible(false);
        name.setVisible(false);
        patronymic.setVisible(false);
        image.setVisible(false);
        avatar.setVisible(false);
    }

    private void configureBinder() {
        binder.forField(login)
                .asRequired("Поле должно быть заполнено")
                .withValidator(userService::isLoginAvailable, "Логин уже занят")
                .bind(User::getLogin, User::setLogin);
        binder.forField(password)
                .asRequired("Поле должно быть заполнено")
                .withValidator(new PasswordValidator(password, passwordConfirm))
                .bind(User::getPassword, User::setPassword);
        binder.forField(role)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getRole, User::setRole);
        binder.addStatusChangeListener(e -> register.setEnabled(binder.isValid()));
    }

    private HorizontalLayout createAvatarLayout() {
        image.setHeight("80px");
        image.setWidth("80px");

        avatar.setWidth("750px");
        avatar.addValueChangeListener(e -> image.setSrc(avatar.getValue()));
        avatar.setMaxLength(255);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(image, avatar);
        horizontalLayout.setAlignItems(Alignment.END);

        return horizontalLayout;
    }

    private void register() {
        try {
            if (isFinishingRegistration) {
                var user = new User();
                binder.writeBean(user);
                userService.saveUser(user);
                UI.getCurrent().navigate(LoginView.class);
            } else {
                surname.setVisible(true);
                name.setVisible(true);
                patronymic.setVisible(true);
                image.setVisible(true);
                avatar.setVisible(true);
                login.setVisible(false);
                password.setVisible(false);
                passwordConfirm.setVisible(false);
                role.setVisible(false);
                isFinishingRegistration = true;
            }
        } catch (ValidationException e) {
        }
    }
}
