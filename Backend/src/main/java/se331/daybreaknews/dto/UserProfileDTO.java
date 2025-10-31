package se331.daybreaknews.dto;

import lombok.Builder;
import lombok.Data;
import se331.daybreaknews.entity.UserRole;

import java.util.Set;

@Data
@Builder
public class UserProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String profileImagePath;
    private Set<UserRole> roles;
    private Long newsReportedCount;
    private Long commentedCount;
}

