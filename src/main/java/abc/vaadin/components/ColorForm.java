package abc.vaadin.components;

import abc.vaadin.data.entity.Color;
import com.vaadin.flow.component.textfield.TextField;

public class ColorForm extends EditForm<Color> {
    private final TextField name;

    public ColorForm() {
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
    public Color createEntity() {
        return new Color();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.name)
                .asRequired("Поле должно быть заполнено")
                .bind(Color::getName, Color::setName);
    }
}