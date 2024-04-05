package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends AbstractRepository<Status> {
    @Override
    @Query(
            "select s from Status s " +
                    "where lower(s.name) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<Status> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select s from Status s " +
            "where lower(s.name) like lower(concat('%', :searchTerm, '%'))")
    List<Status> search(@Param("searchTerm") String searchTerm);
}
