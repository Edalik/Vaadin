package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query("select c from Color c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Color> search(@Param("searchTerm") String searchTerm);
}
