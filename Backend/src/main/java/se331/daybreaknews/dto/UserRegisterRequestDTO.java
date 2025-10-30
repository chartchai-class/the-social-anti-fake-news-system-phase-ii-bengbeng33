package se331.daybreaknews.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String profileImagePath; // optional: public URL or path
}

