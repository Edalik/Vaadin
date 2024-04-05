package abc.vaadin.components;

import abc.vaadin.data.entity.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class ProductEditForm extends EditForm<Product> {
    private final TextField brand;
    private final TextField model;
    private final IntegerField price;
    private final ComboBox<Color> color;
    private final ComboBox<Category> category;
    private final ComboBox<Status> status;
    private final ComboBox<Provider> provider;

    public ProductEditForm(List<Color> colors, List<Category> categories, List<Status> statuses, List<Provider> providers) {
        this.brand = this.createBrandField();
        this.model = this.createModelField();
        this.price = this.createPriceField();
        this.color = this.createColorField(colors);
        this.category = this.createCategoryField(categories);
        this.status = this.createStatusField(statuses);
        this.provider = this.createProviderField(providers);

        add(
                this.brand,
                this.model,
                this.price,
                this.color,
                this.category,
                this.status,
                this.provider
        );

        configureBinder();
    }

    //region Field-components initialization

    private TextField createBrandField() {
        var field = new TextField("Бренд");
        field.setSizeFull();
        return field;
    }

    private TextField createModelField() {
        var field = new TextField("Модель");
        field.setSizeFull();
        return field;
    }

    private IntegerField createPriceField() {
        var field = new IntegerField("Цена");
        field.setSizeFull();
        return field;
    }

    private ComboBox<Color> createColorField(List<Color> colors) {
        var field = new ComboBox<Color>("Цвет");
        field.setItems(colors);
        field.setItemLabelGenerator(Color::getName);
        field.setSizeFull();
        return field;
    }

    private ComboBox<Category> createCategoryField(List<Category> categories) {
        var field = new ComboBox<Category>("Категория");
        field.setItems(categories);
        field.setItemLabelGenerator(Category::getName);
        field.setSizeFull();
        return field;
    }

    private ComboBox<Status> createStatusField(List<Status> statuses) {
        var field = new ComboBox<Status>("Статус");
        field.setItems(statuses);
        field.setItemLabelGenerator(Status::getName);
        field.setSizeFull();
        return field;
    }

    private ComboBox<Provider> createProviderField(List<Provider> providers) {
        var field = new ComboBox<Provider>("Поставщик");
        field.setItems(providers);
        field.setItemLabelGenerator(Provider::toString);
        field.setSizeFull();
        return field;
    }

    //endregion

    @Override
    public Product createEntity() {
        return new Product();
    }

    @Override
    protected void configureBinder() {
        binder.forField(this.brand)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getBrand, Product::setBrand);
        binder.forField(this.model)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getModel, Product::setModel);
        binder.forField(this.price)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getPrice, Product::setPrice);
        binder.forField(this.color)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getColor, Product::setColor);
        binder.forField(this.category)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getCategory, Product::setCategory);
        binder.forField(this.status)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getStatus, Product::setStatus);
        binder.forField(this.provider)
                .asRequired("Поле должно быть заполнено")
                .bind(Product::getProvider, Product::setProvider);
    }
}