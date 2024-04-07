package abc.vaadin.data.repository;

import abc.vaadin.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserRepository extends AbstractRepository<User> {
    @Override
    @Query(
            "select u from User u " +
                    "where lower(u.name) like lower(concat('%', :searchTerm, '%'))" +
                    "or lower(u.surname) like lower(concat('%', :searchTerm, '%'))" +
                    "or lower(u.patronymic) like lower(concat('%', :searchTerm, '%'))" +
                    "or lower(u.login) like lower(concat('%', :searchTerm, '%'))"
    )
    Page<User> find(@Param("searchTerm") String filter, Pageable pageable);

    @Query("select u from User u " +
            "where lower(u.login) like lower(concat('%', :searchTerm, '%'))")
    List<User> search(@Param("searchTerm") String searchTerm);

    @Query("select u from User u where u.login = :login")
    User findByLogin(@Param("login") String login);

    @Modifying
    @Query("update User u set u.surname = :surname, u.name = :name, u.patronymic = :patronymic, u.avatar = :avatar where u.id = :id")
    void updateUser(@Param("surname") String surname,
                    @Param("name") String name,
                    @Param("patronymic") String patronymic,
                    @Param("avatar") String avatar,
                    @Param("id") Long id);
}