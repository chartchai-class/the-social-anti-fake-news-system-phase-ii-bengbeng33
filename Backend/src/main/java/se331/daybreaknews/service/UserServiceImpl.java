package se331.daybreaknews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import se331.daybreaknews.dao.UserDao;
import se331.daybreaknews.dto.UserRegisterRequestDTO;
import se331.daybreaknews.dto.UserDTO;
import se331.daybreaknews.entity.User;
import se331.daybreaknews.entity.UserRole;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO register(UserRegisterRequestDTO request) {
        if (userDao.existsByEmail(normalizeEmail(request.getEmail()))) {
            throw new IllegalArgumentException("Email is already in use");
        }

        String username = generateUniqueUsername(request.getName(), request.getSurname());

        User user = User.builder()
                .name(request.getName().trim())
                .surname(request.getSurname().trim())
                .username(username)
                .email(normalizeEmail(request.getEmail()))
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .profileImagePath(request.getProfileImagePath())
                .build();
        user.getRoles().add(UserRole.READER);

        User saved = userDao.save(user);
        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(normalizeEmail(email));
    }

    @Override
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImagePath(user.getProfileImagePath())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userDao.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateMemberRole(Long userId, boolean makeMember) {
        User user = userDao.getUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        if (makeMember) {
            user.getRoles().add(UserRole.MEMBER);
        } else {
            user.getRoles().remove(UserRole.MEMBER);
        }
        user.getRoles().add(UserRole.READER);

        User saved = userDao.save(user);
        return toDTO(saved);
    }

    private String generateUniqueUsername(String name, String surname) {
        String base = (StringUtils.hasText(name) ? name.trim() : "").concat(" ")
                .concat(StringUtils.hasText(surname) ? surname.trim() : "").trim();
        if (!StringUtils.hasText(base)) {
            base = "User";
        }

        String candidate = normalizeWhitespace(base);
        if (!userDao.existsByUsername(candidate)) {
            return candidate;
        }

        int counter = 2;
        while (userDao.existsByUsername(candidate + " (" + counter + ")")) {
            counter++;
        }
        return candidate + " (" + counter + ")";
    }

    private String normalizeWhitespace(String value) {
        return value.replaceAll("\\s+", " ").trim();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
