package abc.vaadin.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Cart extends AbstractEntity {
    Integer product_id;
    Integer user_id;

    public Cart() {
    }

    public Cart(Integer product_id, Integer user_id) {
        this.product_id = product_id;
        this.user_id = user_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
