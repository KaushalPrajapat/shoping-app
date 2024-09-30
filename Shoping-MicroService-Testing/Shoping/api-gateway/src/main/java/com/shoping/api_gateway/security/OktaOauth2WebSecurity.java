package com.shoping.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class OktaOauth2WebSecurity {

    @SuppressWarnings("removal")
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable() // Disable CSRF if needed
                        .authorizeExchange(exchanges -> exchanges
                                        .pathMatchers(HttpMethod.GET, "/**").permitAll() // Allow all GET requests
                                        .pathMatchers(HttpMethod.POST, "/order/**").authenticated() // Require authentication for POST
                                        .pathMatchers(HttpMethod.PUT, "/order/**").authenticated() // Require authentication for PUT
                                        .pathMatchers(HttpMethod.DELETE, "/order/**").authenticated() // Require authentication for
                                        // DELETE
                                        .anyExchange().authenticated() // Auth for other paths
                        )
                        .oauth2Login(Customizer.withDefaults()) // OAuth2 login
                        .oauth2ResourceServer(server -> server.jwt())); // Okta JWT authentication

        return http.build();
    }
}