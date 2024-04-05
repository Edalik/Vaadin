package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ColorRepository extends AbstractRepository<Color> {
    @Override
    @Query(
            "select c from Color c " +
                    "where lower(c.name) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<Color> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select c from Color c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Color> search(@Param("searchTerm") String searchTerm);
}
