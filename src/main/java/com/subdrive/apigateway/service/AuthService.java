package com.subdrive.apigateway.service;

import com.subdrive.apigateway.domain.LoginDto;
import com.subdrive.apigateway.domain.PasswordResetDto;
import com.subdrive.apigateway.domain.UserDto;
import com.subdrive.apigateway.domain.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Authentication service for the SubDrive API Gateway.
 *
 * <p>This service manages authentication-related operations by communicating with the User Service
 * microservice. It implements both reactive and blocking communication patterns using WebClient.
 *
 * <p>Responsibilities include:
 * <ul>
 *   <li>User registration with reactive response handling</li>
 *   <li>User authentication and token generation</li>
 *   <li>Password reset functionality</li>
 *   <li>Error handling and response transformation</li>
 * </ul>
 *
 * <p>Note: This service uses a mix of reactive and blocking operations. Consider
 * migrating all operations to reactive patterns for improved scalability.
 */
@Service
public class AuthService {

    /**
     * WebClient builder instance for creating HTTP clients.
     * Configured with default settings for service-to-service communication.
     */
    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * Base URL for the User Service microservice.
     * Configured through application properties with the key 'user.service.url'.
     */
    @Value("${user.service.url}")
    private String userServiceUrl;

    /**
     * Registers a new user through the User Service.
     *
     * <p>This method implements a reactive pattern using WebClient, providing:
     * <ul>
     *   <li>Non-blocking HTTP communication</li>
     *   <li>Error handling for unexpected response formats</li>
     *   <li>Response transformation to UserResponse type</li>
     *   <li>Logging for error tracking</li>
     * </ul>
     *
     * <p>TODO:
     * <ul>
     *   <li>Replace hardcoded bearer token with proper service authentication</li>
     *   <li>Implement proper logging mechanism instead of System.out.println</li>
     *   <li>Add circuit breaker pattern for service resilience</li>
     * </ul>
     *
     * @param userRequest the user registration data transfer object
     * @return Mono<UserResponse> containing the registered user's information
     * @throws RuntimeException if the response format is unexpected or service is unavailable
     */
    public Mono<UserResponse> registerUser(UserDto userRequest) {
        return webClientBuilder.build()
                .post()
                .uri(userServiceUrl + "/api/users/register")
                .bodyValue(userRequest)
                .headers(headers -> headers.setBearerAuth("your-token-here"))
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().is2xxSuccessful()) {
                        System.out.println("Received unexpected response format: {}"+ e.getResponseBodyAsString());
                        return Mono.error(new RuntimeException("Unexpected response format from user service"));
                    }
                    return Mono.error(e);
                })
                .doOnError(e -> System.out.println("Error during user registration: "+ e));
    }

    /**
     * Authenticates a user and generates an authentication token.
     *
     * <p>WARNING: This method uses blocking operations (.block()) which is not recommended
     * in a reactive application. Consider refactoring to return Mono<ResponseEntity<?>>
     *
     * @param loginDto the login credentials data transfer object
     * @return ResponseEntity<?> containing either the authentication token or error message
     * @throws RuntimeException if the User Service is unavailable
     */
    public ResponseEntity<?> authenticateUser(LoginDto loginDto) {
        return webClientBuilder.build()
                .post()
                .uri(userServiceUrl + "/api/users/login")
                .body(Mono.just(loginDto), LoginDto.class)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    /**
     * Processes a password reset request.
     *
     * <p>WARNING: This method uses blocking operations (.block()) which is not recommended
     * in a reactive application. Consider refactoring to return Mono<ResponseEntity<?>>
     *
     * <p>The method forwards the reset request to the User Service which handles:
     * <ul>
     *   <li>Token validation</li>
     *   <li>Password update</li>
     *   <li>Response generation</li>
     * </ul>
     *
     * @param passwordResetDto the password reset data transfer object
     * @return ResponseEntity<?> containing either a success message or error details
     * @throws RuntimeException if the User Service is unavailable
     */
    public ResponseEntity<?> resetPassword(PasswordResetDto passwordResetDto) {
        return webClientBuilder.build()
                .post()
                .uri(userServiceUrl + "/api/users/reset-password")
                .body(Mono.just(passwordResetDto), PasswordResetDto.class)
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}