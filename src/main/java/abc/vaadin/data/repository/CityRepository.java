package abc.vaadin.data.repository;

import abc.vaadin.data.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("select c from City c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<City> search(@Param("searchTerm") String searchTerm);
}
