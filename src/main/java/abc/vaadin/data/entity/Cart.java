package abc.vaadin.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Cart extends AbstractEntity{
    Long product_id;
    Long user_id;

    public Cart() {
    }

    public Cart(Long product_id, Long user_id) {
        this.product_id = product_id;
        this.user_id = user_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
