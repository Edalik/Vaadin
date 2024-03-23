package abc.vaadin.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Status extends AbstractEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "status")
    private List<Product> statuses = new LinkedList<>();

    public List<Product> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Product> statuses) {
        this.statuses = statuses;
    }
}
