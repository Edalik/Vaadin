package abc.vaadin.data.repository;

import abc.vaadin.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.login = :login")
    User findByLogin(@Param("login") String login);

    @Query("select u from User u " +
            "where lower(u.login) like lower(concat('%', :searchTerm, '%'))")
    List<User> search(@Param("searchTerm") String searchTerm);

    @Modifying
    @Query("update User u set u.surname = :surname, u.name = :name, u.patronymic = :patronymic, u.avatar = :avatar where u.id = :id")
    void updateUser(@Param("surname") String surname,
                    @Param("name") String name,
                    @Param("patronymic") String patronymic,
                    @Param("avatar") String avatar,
                    @Param("id") Integer id);
}
