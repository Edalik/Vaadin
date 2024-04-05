package abc.vaadin.data.repository;

import abc.vaadin.data.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;


@NoRepositoryBean
public interface AbstractRepository<TEntity extends AbstractEntity> extends
        JpaRepository<TEntity, Long>,
        JpaSpecificationExecutor<TEntity> {
    Page<TEntity> find(@Param("searchTerm") String filter, Pageable pageable);
}
