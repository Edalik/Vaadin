package abc.vaadin.components;

import abc.vaadin.data.entity.Color;
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

public class ColorForm extends FormLayout {
    Binder<Color> binder = new Binder<>(Color.class);
    TextField name = new TextField("Цвет");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    public ColorForm() {
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setColor(Color name) {
        binder.setBean(name);
    }

    public static abstract class ColorFormEvent extends ComponentEvent<ColorForm> {
        private Color color;

        protected ColorFormEvent(ColorForm source, Color color) {
            super(source, false);
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    public static class SaveEvent extends ColorFormEvent {
        SaveEvent(ColorForm source, Color color) {
            super(source, color);
        }
    }

    public static class DeleteEvent extends ColorFormEvent {
        DeleteEvent(ColorForm source, Color color) {
            super(source, color);
        }

    }

    public static class CloseEvent extends ColorFormEvent {
        CloseEvent(ColorForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}