package abc.vaadin.views.registration;

import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class PasswordValidator implements Validator<String> {
    private final PasswordField password;
    private final PasswordField passwordConfirm;


    public PasswordField getPassword() {
        return password;
    }

    public PasswordField getPasswordConfirm() {
        return passwordConfirm;
    }

    public PasswordValidator(PasswordField password, PasswordField passwordConfirm) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public ValidationResult apply(String s, ValueContext valueContext) {

        if (getPassword().getValue().equals(getPasswordConfirm().getValue()))
            return ValidationResult.ok();

        return ValidationResult.error("Пароли не совпадают");
    }
}
