package abc.vaadin.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Product extends AbstractEntity {
    private String brand;
    private String model;
    private Integer price;
    @ManyToOne
    @JoinColumn(name = "color_id")
    @NotNull
    @JsonIgnoreProperties({"colors"})
    private Color color;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    @JsonIgnoreProperties({"categories"})
    private Category category;
    @ManyToOne
    @JoinColumn(name = "status_id")
    @NotNull
    @JsonIgnoreProperties({"statuses"})
    private Status status;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    @NotNull
    @JsonIgnoreProperties({"providers"})
    private Provider provider;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "cart",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "Product{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", status=" + status +
                '}';
    }
}
