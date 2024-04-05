package abc.vaadin.components;

import abc.vaadin.data.entity.City;
import abc.vaadin.data.entity.Provider;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class ProviderForm extends EditForm<Provider> {
    private final TextField name;
    private final ComboBox<City> city;
    private final EmailField email;
    private final TextField number;

    public ProviderForm(List<City> cities) {
        this.name = this.createNameField();
        this.city = this.createCityField(cities);
        this.email = this.createEmailField();
        this.number = this.createNumberField();

        add(
                this.name,
                this.city,
                this.email,
                this.number
        );

        configureBinder();
    }

    //region Field-components initialization

    private TextField createNameField() {
        var field = new TextField("Название");
        field.setSizeFull();
        return field;
    }

    private ComboBox<City> createCityField(List<City> cities) {
        var field = new ComboBox<City>("Город");
        field.setItems(cities);
        field.setItemLabelGenerator(City::getName);
        field.setSizeFull();
        return field;
    }

    private EmailField createEmailField() {
        var field = new EmailField("Почта");
        field.setSizeFull();
        return field;
    }

    private TextField createNumberField() {
        var field = new TextField("Номер");
        field.setMinLength(11);
        field.setMaxLength(11);
        field.setAllowedCharPattern("^[0-9]*$");
        field.setSizeFull();
        return field;
    }

    //endregion

    @Override
    public Provider createEntity() {
        return new Provider();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.name)
                .asRequired("Поле должно быть заполнено")
                .bind(Provider::getName, Provider::setName);
        binder.forField(this.city)
                .asRequired("Поле должно быть заполнено")
                .bind(Provider::getCity, Provider::setCity);
        binder.forField(this.email)
                .asRequired("Поле должно быть заполнено")
                .bind(Provider::getEmail, Provider::setEmail);
        binder.forField(this.number)
                .asRequired("Поле должно быть заполнено")
                .bind(Provider::getNumber, Provider::setNumber);
    }
}