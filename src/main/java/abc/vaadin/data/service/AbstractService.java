package abc.vaadin.data.service;

import abc.vaadin.data.entity.AbstractEntity;
import abc.vaadin.data.repository.AbstractRepository;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.Stream;


public abstract class AbstractService<TEntity extends AbstractEntity> extends AbstractBackEndDataProvider<TEntity, String> {
    private final AbstractRepository<TEntity> repository;

    protected AbstractService(AbstractRepository<TEntity> repository) {
        this.repository = repository;
    }

    public TEntity findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public boolean contains(TEntity entity) {
        return entity != null && entity.getId() != null && findById(entity.getId()) != null;
    }

    public TEntity update(TEntity entity) {
        return repository.save(entity);
    }

    public void delete(TEntity entity) {
        repository.delete(entity);
    }

    public Page<TEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<TEntity> findAll(String filter, Pageable pageable) {
        return repository.find(filter, pageable);
    }

    @Override
    protected Stream<TEntity> fetchFromBackEnd(Query<TEntity, String> query) {
        var page = PageRequest.of(query.getPage(), query.getPageSize());
        return query.getFilter().isPresent() ?
                findAll(query.getFilter().get(), page).stream() :
                findAll(page).stream();

    }

    @Override
    protected int sizeInBackEnd(Query<TEntity, String> query) {
        return (int) fetchFromBackEnd(query).count();
    }
}
