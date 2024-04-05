package abc.vaadin.components;

import abc.vaadin.data.entity.Status;
import com.vaadin.flow.component.textfield.TextField;

public class StatusForm extends EditForm<Status> {
    private final TextField name;

    public StatusForm() {
        this.name = this.createNameField();

        add(
                this.name
        );

        configureBinder();
    }

    //region Field-components initialization

    private TextField createNameField() {
        var field = new TextField("Название");
        field.setSizeFull();
        return field;
    }

    //endregion

    @Override
    public Status createEntity() {
        return new Status();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.name)
                .asRequired("Поле должно быть заполнено")
                .bind(Status::getName, Status::setName);
    }
}