package abc.vaadin.components;

import abc.vaadin.data.entity.City;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class CityForm extends FormLayout {
    Binder<City> binder = new Binder<>(City.class);
    TextField name = new TextField("Город");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    public CityForm() {
        binder.bindInstanceFields(this);
        add(name, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CityForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CityForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setCity(City city) {
        binder.setBean(city);
    }

    public static abstract class CityFormEvent extends ComponentEvent<CityForm> {
        private City city;

        protected CityFormEvent(CityForm source, City city) {
            super(source, false);
            this.city = city;
        }

        public City getCity() {
            return city;
        }
    }

    public static class SaveEvent extends CityForm.CityFormEvent {
        SaveEvent(CityForm source, City city) {
            super(source, city);
        }
    }

    public static class DeleteEvent extends CityForm.CityFormEvent {
        DeleteEvent(CityForm source, City city) {
            super(source, city);
        }

    }

    public static class CloseEvent extends CityForm.CityFormEvent {
        CloseEvent(CityForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<CityForm.DeleteEvent> listener) {
        return addListener(CityForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<CityForm.SaveEvent> listener) {
        return addListener(CityForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CityForm.CloseEvent> listener) {
        return addListener(CityForm.CloseEvent.class, listener);
    }
}
