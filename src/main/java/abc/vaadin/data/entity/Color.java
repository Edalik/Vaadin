package abc.vaadin.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Color extends AbstractEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "color")
    private List<Product> colors = new LinkedList<>();

    public List<Product> getColors() {
        return colors;
    }

    public void setColors(List<Product> colors) {
        this.colors = colors;
    }
}