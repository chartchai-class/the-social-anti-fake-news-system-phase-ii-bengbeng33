package se331.daybreaknews.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}

