package abc.vaadin.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.LinkedList;
import java.util.List;

@Entity
public class City extends AbstractEntity {
    private String name;
    @OneToMany(mappedBy = "city")
    private List<Provider> cities = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Provider> getCities() {
        return cities;
    }

    public void setCities(List<Provider> cities) {
        this.cities = cities;
    }
}