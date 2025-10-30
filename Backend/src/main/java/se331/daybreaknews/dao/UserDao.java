package se331.daybreaknews.dao;

import se331.daybreaknews.entity.User;

import java.util.Optional;

public interface UserDao {
    // try to get a user optional if it's match
    Optional<User> getUser(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user);
}

