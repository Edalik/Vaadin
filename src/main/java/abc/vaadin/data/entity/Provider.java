package abc.vaadin.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Provider extends AbstractEntity {
    private String name;
    @ManyToOne
    @JoinColumn(name = "city_id")
    @NotNull
    @JsonIgnoreProperties({"cities"})
    private City city;
    private String email;
    private String number;
    @OneToMany(mappedBy = "provider")
    private List<Product> providers = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Product> getProviders() {
        return providers;
    }

    public void setProviders(List<Product> providers) {
        this.providers = providers;
    }

    @Override
    public String toString() {
        return getName() + ", " + getEmail() + ", " + getNumber() + ", " + getCity().getName();
    }
}