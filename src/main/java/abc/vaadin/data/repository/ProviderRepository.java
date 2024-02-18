package abc.vaadin.data.repository;

import abc.vaadin.data.entity.City;
import abc.vaadin.data.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    @Query("select p from Provider p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%'))")
    List<Provider> search(@Param("searchTerm") String searchTerm);
}
