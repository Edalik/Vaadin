package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends AbstractRepository<Provider> {
    @Override
    @Query(
            "select p from Provider p " +
                    "where lower(p.name) like lower(concat('%', :searchTerm, '%'))" +
                    "or lower(p.email) like lower(concat('%', :searchTerm, '%'))"+
                    "or lower(p.number) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<Provider> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select p from Provider p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%'))")
    List<Provider> search(@Param("searchTerm") String searchTerm);
}
