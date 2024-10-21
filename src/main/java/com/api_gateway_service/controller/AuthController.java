package com.api_gateway_service.controller;

import com.api_gateway_service.domain.LoginDto;
import com.api_gateway_service.domain.PasswordResetDto;
import com.api_gateway_service.domain.UserDto;
import com.api_gateway_service.domain.UserResponse;
import com.api_gateway_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> registerUser(@RequestBody UserDto userRequest) {
        return authService.registerUser(userRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        return authService.authenticateUser(loginDto);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        return authService.resetPassword(passwordResetDto);
    }
}
