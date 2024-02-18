package abc.vaadin.components;

import abc.vaadin.data.entity.Role;
import abc.vaadin.data.entity.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class UserForm  extends FormLayout {
    Binder<User> binder = new Binder<>(User.class);
    TextField surname = new TextField("Фамилия");
    TextField name = new TextField("Имя");
    TextField patronymic = new TextField("Отчество");
    TextField avatar = new TextField("Аватар");
    TextField login = new TextField("Логин");
    PasswordField password = new PasswordField("Пароль");
    ComboBox<Role> role =new ComboBox<>("Роль");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    public UserForm() {
        role.setItems(Role.USER);
        role.setValue(Role.USER);
        
        binder.bindInstanceFields(this);
        add(surname, name, patronymic, avatar, login, password, role, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new UserForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new UserForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new UserForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setUser(User user) {
        binder.setBean(user);
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserForm.UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserForm.UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }

    }

    public static class CloseEvent extends UserForm.UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<UserForm.DeleteEvent> listener) {
        return addListener(UserForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<UserForm.SaveEvent> listener) {
        return addListener(UserForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<UserForm.CloseEvent> listener) {
        return addListener(UserForm.CloseEvent.class, listener);
    }
}
