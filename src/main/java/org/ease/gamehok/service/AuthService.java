package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.AuthRequest;
import org.ease.gamehok.dto.AuthResponse;
import org.ease.gamehok.entity.Role;
import org.ease.gamehok.entity.User;
import org.ease.gamehok.repository.UserRepository;
import org.ease.gamehok.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public User register(User user) {

        user.setPassword(
                encoder.encode(user.getPassword())
        );

        user.setRole(Role.PLAYER);

        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElseThrow(() ->
                new RuntimeException("User not found")
        );

        if (!encoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}