package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends AbstractRepository<Product> {
    @Override
    @Query(
            "select p from Product p " +
                    "where lower(p.brand) like lower(concat('%', :searchTerm, '%'))" +
                    "or lower(p.model) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<Product> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select p from Product p " +
            "where lower(p.brand) like lower(concat('%', :searchTerm, '%'))")
    List<Product> search(@Param("searchTerm") String searchTerm);
}