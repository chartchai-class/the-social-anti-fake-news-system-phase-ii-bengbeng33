package se331.daybreaknews.dto;

import lombok.Builder;
import lombok.Data;
import se331.daybreaknews.entity.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String profileImagePath;
    private LocalDateTime createdAt;
    private Set<UserRole> roles;
}
