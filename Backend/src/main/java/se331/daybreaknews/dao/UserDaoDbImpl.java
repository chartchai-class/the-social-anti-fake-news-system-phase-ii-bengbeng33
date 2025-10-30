package se331.daybreaknews.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import se331.daybreaknews.entity.User;
import se331.daybreaknews.repository.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class UserDaoDbImpl implements UserDao {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return email == null ? Optional.empty() : userRepository.findByEmailIgnoreCase(email.trim());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return username == null ? Optional.empty() : userRepository.findByUsernameIgnoreCase(username.trim());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
