package abc.vaadin.components;

import abc.vaadin.data.entity.Category;
import com.vaadin.flow.component.textfield.TextField;

public class CategoryForm extends EditForm<Category> {
    private final TextField name;

    public CategoryForm() {
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
    public Category createEntity() {
        return new Category();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.name)
                .asRequired("Поле должно быть заполнено")
                .bind(Category::getName, Category::setName);
    }
}