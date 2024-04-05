package abc.vaadin.components;

import abc.vaadin.data.entity.Role;
import abc.vaadin.data.entity.User;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class UserForm extends EditForm<User> {
    private final TextField name;
    private final TextField surname;
    private final TextField patronymic;
    private final TextField avatar;
    private final TextField login;
    private final PasswordField password;
    private final ComboBox<Role> role;

    public UserForm() {
        this.name = this.createNameField();
        this.surname = this.createSurnameField();
        this.patronymic = this.createPatronymicField();
        this.avatar = this.createAvatarField();
        this.login = this.createLoginField();
        this.password = this.createPasswordField();
        this.role = this.createRoleField();

        add(
                this.name,
                this.surname,
                this.patronymic,
                this.avatar,
                this.login,
                this.password,
                this.role
        );

        configureBinder();
    }

    //region Field-components initialization

    private TextField createNameField() {
        var field = new TextField("Имя");
        field.setSizeFull();
        return field;
    }

    private TextField createSurnameField() {
        var field = new TextField("Фамилия");
        field.setSizeFull();
        return field;
    }

    private TextField createPatronymicField() {
        var field = new TextField("Отчество");
        field.setSizeFull();
        return field;
    }

    private TextField createAvatarField() {
        var field = new TextField("Аватар");
        field.setSizeFull();
        return field;
    }

    private TextField createLoginField() {
        var field = new TextField("Логин");
        field.setSizeFull();
        return field;
    }

    private PasswordField createPasswordField() {
        var field = new PasswordField("Пароль");
        field.setSizeFull();
        return field;
    }

    private ComboBox<Role> createRoleField() {
        var field = new ComboBox<Role>("Роль");
        field.setItems(Role.values());
        field.setItemLabelGenerator(Role::toString);
        field.setSizeFull();
        return field;
    }

    //endregion

    @Override
    public User createEntity() {
        return new User();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.name)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getName, User::setName);
        binder.forField(this.surname)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getSurname, User::setSurname);
        binder.forField(this.patronymic)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getPatronymic, User::setPatronymic);
        binder.forField(this.avatar)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getAvatar, User::setAvatar);
        binder.forField(this.login)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getLogin, User::setLogin);
        binder.forField(this.password)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getPassword, User::setPassword);
        binder.forField(this.role)
                .asRequired("Поле должно быть заполнено")
                .bind(User::getRole, User::setRole);
    }
}