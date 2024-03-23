package abc.vaadin.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Category extends AbstractEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    private List<Product> categories = new LinkedList<>();

    public List<Product> getCategories() {
        return categories;
    }

    public void setCategories(List<Product> categories) {
        this.categories = categories;
    }
}