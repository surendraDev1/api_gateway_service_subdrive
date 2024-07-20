package com.consultingfirm.api_gateway_service;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/test/**","/swagger-ui/**","/v3/api-docs/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("http://localhost:9091")) //user details microservice port number
//                        .uri("lb://subdrive-rentals"))
                .build();
    }
}
