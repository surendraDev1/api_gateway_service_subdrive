package com.subdrive.apigateway.controller;

import com.subdrive.apigateway.domain.LoginDto;
import com.subdrive.apigateway.domain.PasswordResetDto;
import com.subdrive.apigateway.domain.UserDto;
import com.subdrive.apigateway.domain.UserResponse;
import com.subdrive.apigateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * API Gateway controller handling authentication-related requests.
 *
 * <p>This controller acts as the entry point for authentication operations in the API Gateway,
 * routing and managing authentication requests to appropriate microservices. It supports:
 * <ul>
 *   <li>User registration</li>
 *   <li>User authentication (login)</li>
 *   <li>Password reset</li>
 * </ul>
 *
 * <p>The controller implements reactive programming patterns using Spring WebFlux
 * for non-blocking request handling.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Service handling authentication business logic and microservice communication.
     */
    @Autowired
    private AuthService authService;

    /**
     * Handles user registration requests.
     *
     * <p>This endpoint:
     * <ul>
     *   <li>Processes new user registration requests</li>
     *   <li>Returns user information upon successful registration</li>
     *   <li>Implements reactive programming with Mono for non-blocking operation</li>
     * </ul>
     *
     * @param userRequest DTO containing user registration information
     * @return Mono<ResponseEntity<UserResponse>> containing:
     *         <ul>
     *           <li>200 (OK) with user details on successful registration</li>
     *           <li>400 (Bad Request) if registration fails or validation errors occur</li>
     *         </ul>
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> registerUser(@RequestBody UserDto userRequest) {
        return authService.registerUser(userRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    /**
     * Authenticates a user and provides access credentials.
     *
     * <p>This endpoint:
     * <ul>
     *   <li>Validates user credentials</li>
     *   <li>Generates authentication tokens upon successful login</li>
     *   <li>Handles various authentication failure scenarios</li>
     * </ul>
     *
     * @param loginDto DTO containing user login credentials
     * @return ResponseEntity<?> containing:
     *         <ul>
     *           <li>200 (OK) with authentication token on successful login</li>
     *           <li>401 (Unauthorized) for invalid credentials</li>
     *           <li>400 (Bad Request) for malformed requests</li>
     *         </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        return authService.authenticateUser(loginDto);
    }

    /**
     * Processes password reset requests.
     *
     * <p>This endpoint:
     * <ul>
     *   <li>Validates password reset tokens</li>
     *   <li>Updates user passwords</li>
     *   <li>Handles token expiration and validation</li>
     * </ul>
     *
     * @param passwordResetDto DTO containing reset token and new password
     * @return ResponseEntity<?> containing:
     *         <ul>
     *           <li>200 (OK) on successful password reset</li>
     *           <li>400 (Bad Request) for invalid or expired tokens</li>
     *           <li>404 (Not Found) if user not found</li>
     *         </ul>
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        return authService.resetPassword(passwordResetDto);
    }
}