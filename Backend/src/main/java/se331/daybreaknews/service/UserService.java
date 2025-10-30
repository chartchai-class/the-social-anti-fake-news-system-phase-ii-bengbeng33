package se331.daybreaknews.service;

import se331.daybreaknews.dto.UserRegisterRequestDTO;
import se331.daybreaknews.dto.UserDTO;
import se331.daybreaknews.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO register(UserRegisterRequestDTO request);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    UserDTO toDTO(User user);
    List<UserDTO> getAllUsers();
    UserDTO updateMemberRole(Long userId, boolean makeMember);
    UserDTO updateVerifiedStatus(Long userId, boolean verified);
}
