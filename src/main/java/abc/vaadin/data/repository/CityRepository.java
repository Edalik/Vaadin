package abc.vaadin.data.repository;

import abc.vaadin.data.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends AbstractRepository<City> {
    @Override
    @Query(
            "select c from City c " +
                    "where lower(c.name) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<City> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select c from City c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<City> search(@Param("searchTerm") String searchTerm);
}
