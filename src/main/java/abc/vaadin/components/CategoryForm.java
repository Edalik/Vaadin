package abc.vaadin.components;

import abc.vaadin.data.entity.Category;
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

public class CategoryForm extends FormLayout {
    Binder<Category> binder = new Binder<>(Category.class);
    TextField name = new TextField("Категория");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отменить");

    public CategoryForm() {
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

    public void setCategory(Category name) {
        binder.setBean(name);
    }

    public static abstract class CategoryFormEvent extends ComponentEvent<CategoryForm> {
        private Category category;

        protected CategoryFormEvent(CategoryForm source, Category category) {
            super(source, false);
            this.category = category;
        }

        public Category getCategory() {
            return category;
        }
    }

    public static class SaveEvent extends CategoryFormEvent {
        SaveEvent(CategoryForm source, Category category) {
            super(source, category);
        }
    }

    public static class DeleteEvent extends CategoryFormEvent {
        DeleteEvent(CategoryForm source, Category category) {
            super(source, category);
        }

    }

    public static class CloseEvent extends CategoryFormEvent {
        CloseEvent(CategoryForm source) {
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
