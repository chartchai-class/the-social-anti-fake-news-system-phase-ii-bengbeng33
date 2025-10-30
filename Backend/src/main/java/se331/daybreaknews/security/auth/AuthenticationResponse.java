package se331.daybreaknews.security.auth;

import se331.daybreaknews.dto.UserDTO;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        UserDTO user
) {
    public static AuthenticationResponse of(String accessToken, String refreshToken, UserDTO user) {
        return new AuthenticationResponse(accessToken, refreshToken, user);
    }
}
