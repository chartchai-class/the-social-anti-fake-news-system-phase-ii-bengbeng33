package se331.daybreaknews.service;

import se331.daybreaknews.dto.LoginRequest;
import se331.daybreaknews.dto.RegisterRequest;
import se331.daybreaknews.dto.UserDTO;
import se331.daybreaknews.entity.User;

import java.util.Optional;

public interface UserService {
    UserDTO register(RegisterRequest request);
    UserDTO login(LoginRequest request);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    UserDTO toDTO(User user);
}

