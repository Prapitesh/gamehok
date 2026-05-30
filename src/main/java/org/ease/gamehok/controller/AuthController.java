package org.ease.gamehok.controller;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.AuthRequest;
import org.ease.gamehok.dto.AuthResponse;
import org.ease.gamehok.entity.User;
import org.ease.gamehok.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public User register(
            @RequestBody User user
    ) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request
    ) {
        return authService.login(request);
    }
}