package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends AbstractRepository<Category> {
    @Override
    @Query(
            "select c from Category c " +
                    "where lower(c.name) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<Category> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select c from Category c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Category> search(@Param("searchTerm") String searchTerm);
}