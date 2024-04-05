package abc.vaadin.components;

import abc.vaadin.data.entity.AbstractEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

public abstract class EditForm<TEntity extends AbstractEntity> extends Dialog {
    private final VerticalLayout layout = createLayout();
    private final Button delete = createDeleteButton();
    private final Button close = createCloseButton();
    @SuppressWarnings("unchecked")
    protected Binder<TEntity> binder = new BeanValidationBinder<>((Class<TEntity>) createEntity().getClass());
    private TEntity entity;
    private final Button save = createSaveButton();

    protected EditForm() {
        super.add(layout);
        layout.setSizeFull();

        getHeader().add(createHeader());
        getFooter().add(createFooter());

        setResizable(true);
        setDraggable(true);
        setWidth("800px");
    }

    protected abstract void configureBinder();

    //region Field-components initialization

    private VerticalLayout createLayout() {
        return new VerticalLayout();
    }

    private Button createSaveButton() {
        var save = new Button("Сохранить");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        return save;
    }

    private Button createDeleteButton() {
        var delete = new Button("Удалить");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new EditFormEvents.DeleteEvent(this)));

        return delete;
    }

    private Button createCloseButton() {
        var close = new Button("Отмена");
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);
        close.addClickListener(event -> close());

        return close;
    }

    //endregion

    //region Creating a form

    private Component createHeader() {
        var h1 = new H3("Редактирование");
        h1.addClassNames(LumoUtility.TextAlignment.CENTER);
        h1.setSizeFull();
        return h1;
    }

    private HorizontalLayout createFooter() {
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    //endregion

    //region Inner entity manipulation

    public abstract TEntity createEntity();

    public TEntity getEntity() {
        return entity;
    }

    public void setEntity(TEntity entity) {
        this.entity = entity;
        binder.readBean(this.entity);
    }

    //endregion

    private void validateAndSave() {
        try {
            binder.writeBean(entity);
            fireEvent(new EditFormEvents.SaveEvent(this));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <TEvent extends ComponentEvent<?>> Registration addListener(
            Class<TEvent> eventType,
            ComponentEventListener<TEvent> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }

    @Override
    public void add(Component... components) {
        layout.add(components);
    }
}
