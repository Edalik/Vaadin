package abc.vaadin.components;

import abc.vaadin.data.entity.City;
import com.vaadin.flow.component.textfield.TextField;

public class CityForm extends EditForm<City> {
    private final TextField name;

    public CityForm() {
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
    public City createEntity() {
        return new City();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.name)
                .asRequired("Поле должно быть заполнено")
                .bind(City::getName, City::setName);
    }
}