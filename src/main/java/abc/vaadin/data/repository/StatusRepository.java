package abc.vaadin.data.repository;

import abc.vaadin.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("select s from Status s " +
            "where lower(s.name) like lower(concat('%', :searchTerm, '%'))")
    List<Status> search(@Param("searchTerm") String searchTerm);
}
