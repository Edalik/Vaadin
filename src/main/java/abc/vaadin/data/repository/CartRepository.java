package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Cart;
import abc.vaadin.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("select p from Product p, Cart c where c.user_id = :user_id and p.id = c.product_id")
    List<Product> findByUserID(@Param("user_id") Integer user_id);

    @Query("select c from Cart c where c.product_id = :product_id and c.user_id = :user_id")
    Cart getByIDs(@Param("product_id") Integer product_id,
                  @Param("user_id") Integer user_id);
}
