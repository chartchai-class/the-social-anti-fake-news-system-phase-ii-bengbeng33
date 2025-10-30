package se331.daybreaknews.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se331.daybreaknews.dto.UserRegisterRequestDTO;
import se331.daybreaknews.dto.UserDTO;
import se331.daybreaknews.repository.UserRepository;
import se331.daybreaknews.security.jwt.JwtService;
import se331.daybreaknews.security.user.UserPrincipal;
import se331.daybreaknews.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Transactional
    public AuthenticationResponse register(UserRegisterRequestDTO request) {
        UserDTO registered = userService.register(request);
        var user = userRepository.findByEmail(registered.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found after registration"));

        UserPrincipal principal = UserPrincipal.fromUser(user);
        String jwtToken = jwtService.generateToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);
        return AuthenticationResponse.of(jwtToken, refreshToken, registered);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        UserPrincipal principal = UserPrincipal.fromUser(user);
        String jwtToken = jwtService.generateToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);

        UserDTO userDto = userService.toDTO(user);
        return AuthenticationResponse.of(jwtToken, refreshToken, userDto);
    }
}
