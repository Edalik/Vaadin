package abc.vaadin.components;

import abc.vaadin.data.entity.City;
import abc.vaadin.data.entity.Color;
import abc.vaadin.data.entity.Provider;
import abc.vaadin.data.entity.Provider;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ProviderForm extends FormLayout {
    Binder<Provider> binder = new Binder<>(Provider.class);
    TextField name = new TextField("Название");
    ComboBox<City> city = new ComboBox<>("Город");
    EmailField email = new EmailField("Почта");
    TextField number = new TextField("Телефон");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    public ProviderForm(List<City> cities) {
        binder.bindInstanceFields(this);

        city.setItems(cities);
        city.setItemLabelGenerator(City::getName);

        Div div = new Div();
        div.setText("+7");
        number.setPrefixComponent(div);
        number.setMinLength(10);
        number.setMaxLength(10);
        number.setAllowedCharPattern("^[0-9]*$");

        add(name, city, email, number, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new ProviderForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new ProviderForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            binder.getBean().setNumber("+7" + binder.getBean().getNumber());
            fireEvent(new ProviderForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setProvider(Provider provider) {
        binder.setBean(provider);
    }

    public static abstract class ProviderFormEvent extends ComponentEvent<ProviderForm> {
        private Provider provider;

        protected ProviderFormEvent(ProviderForm source, Provider provider) {
            super(source, false);
            this.provider = provider;
        }

        public Provider getProvider() {
            return provider;
        }
    }

    public static class SaveEvent extends ProviderForm.ProviderFormEvent {
        SaveEvent(ProviderForm source, Provider provider) {
            super(source, provider);
        }
    }

    public static class DeleteEvent extends ProviderForm.ProviderFormEvent {
        DeleteEvent(ProviderForm source, Provider provider) {
            super(source, provider);
        }

    }

    public static class CloseEvent extends ProviderForm.ProviderFormEvent {
        CloseEvent(ProviderForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<ProviderForm.DeleteEvent> listener) {
        return addListener(ProviderForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<ProviderForm.SaveEvent> listener) {
        return addListener(ProviderForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<ProviderForm.CloseEvent> listener) {
        return addListener(ProviderForm.CloseEvent.class, listener);
    }
}
