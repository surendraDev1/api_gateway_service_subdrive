package com.api_gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
/** Routing and Load Balancing
 * The RouteLocator defines routes for incoming requests.
 * For example, the gatewayRouter method creates routes that match specific paths and direct them to backend services (e.g., lb://userService).
 */
@Configuration
@CrossOrigin
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/test/**","/swagger-ui/**","/v3/api-docs/**")
//                        .filters(GatewayFilterSpec::tokenRelay) authorized token will be passed to the path
                        .uri("lb://userService"))
                .build();
    }
}
