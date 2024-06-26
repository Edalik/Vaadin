package abc.vaadin.data.service;

import abc.vaadin.data.entity.User;
import abc.vaadin.data.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends AbstractService<User> implements UserDetailsService {
    protected UserService(UserRepository repository, PasswordEncoder passwordEncoder,
                          UserRepository userRepository) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public boolean isLoginAvailable(String login) {
        return userRepository.findByLogin(login) == null;
    }

    private String getEncodedPassword(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

    public void saveUser(User user) {
        user.setPassword(getEncodedPassword(user));
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public List<User> findAllUsers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(stringFilter);
        }
    }

    public void updateUser(String surname, String name, String patronymic, String avatar, Long id) {
        userRepository.updateUser(surname, name, patronymic, avatar, id);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }
}
