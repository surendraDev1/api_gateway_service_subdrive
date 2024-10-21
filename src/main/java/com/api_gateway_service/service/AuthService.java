package com.api_gateway_service.service;

import com.api_gateway_service.domain.LoginDto;
import com.api_gateway_service.domain.PasswordResetDto;
import com.api_gateway_service.domain.UserDto;
import com.api_gateway_service.domain.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Service
public class AuthService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${user.service.url}")
    private String userServiceUrl;

//    public ResponseEntity<?> registerUser(UserDto userDto) {
//        return webClientBuilder.build()
//                .post()
//                .uri(userServiceUrl + "/api/users/register")
//                .body(Mono.just(userDto), UserDto.class)
//                .retrieve()
//                .toEntity(String.class)
//                .block();
//    }
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
                        // Log the response body for debugging
                        System.out.println("Received unexpected response format: {}"+ e.getResponseBodyAsString());
                        // Try to parse the response manually
                        // This is a fallback and should be removed once the main issue is fixed
                        return Mono.error(new RuntimeException("Unexpected response format from user service"));
                    }
                    return Mono.error(e);
                })
                .doOnError(e -> System.out.println("Error during user registration: "+ e));
    }

    public ResponseEntity<?> authenticateUser(LoginDto loginDto) {
        return webClientBuilder.build()
                .post()
                .uri(userServiceUrl + "/api/users/login")
                .body(Mono.just(loginDto), LoginDto.class)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

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
