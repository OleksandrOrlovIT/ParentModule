package orlov.oleksandr.programming.backendgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import orlov.oleksandr.programming.backendgateway.config.filter.OAuth2GatewayFilter;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("crime-rate-service", r -> r
                        .path("/api/crime-rate/**", "/api/crime-rate")
                        .uri("http://localhost:3050")
                )
                .route("city-country-service", r -> r
                        .path("/api/city/**", "/api/country/**", "/api/country", "/api/city")
                        .uri("http://localhost:8090")
                )
                .route("react-front-end", r -> r
                        .path("/front-end/**", "/front-end")
                        .uri("http://localhost:3000")
                )
                .route("gateway", r -> r
                        .path("/api/**", "/api")
                        .filters(f -> f
                                .rewritePath("/api/(?<segment>.*)", "/${segment}")
                                .filter(new OAuth2GatewayFilter())
                        )
                        .uri("lb://spring-gateway")
                )
                .route("frontend", r -> r
                        .path("/")
                        .filters(f -> f
                                .rewritePath("/api/(?<segment>.*)", "/${segment}")
                                .filter(new OAuth2GatewayFilter())
                        )
                        .uri("http://localhost:3000")
                )
                .build();
    }
}