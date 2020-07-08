package com.example.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Value("${management.endpoints.web.base-path:/actuator}")
    private String managementEndpointsBasePath;


    @Order(2)
    @Bean
    public SecurityWebFilterChain actuatorSecurity(ServerHttpSecurity http) {
        return http

                .securityMatcher(pathMatchers(managementEndpointsBasePath + "/**"))
                .authorizeExchange()

                .pathMatchers(managementEndpointsBasePath + "/loggers/{*logname}")
                .hasRole("ENDPOINT_ADMIN")

                .pathMatchers(managementEndpointsBasePath + "/prometheus")
                .permitAll()

                .anyExchange()
                .permitAll()

                .and()
                .httpBasic()

                .and()
                .build();
    }

    @Order(3)
    @Bean
    @Profile("pro")
    public SecurityWebFilterChain swaggerSecurity(ServerHttpSecurity http) {
        return http

                .securityMatcher(pathMatchers("/swagger-ui.html", "/v2/**"))

                .addFilterAt((exchange, chain) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                    return Mono.empty();
                }, AUTHENTICATION)

                .build();
    }

    @Bean
    public CorsConfigurationSource corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // Possibly...
        // config.applyPermitDefaultValues()

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

