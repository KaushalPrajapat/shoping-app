package com.shoping.order_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @SuppressWarnings({ "deprecation", "removal" })
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                        .anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();

        // new way of writing
        // http
        // .authorizeHttpRequests(auth -> auth
        // .anyRequest().authenticated() // Securing all requests
        // )
        // .oauth2ResourceServer(oauth2 -> oauth2
        // .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) //
        // Customize authentication entry point
        // .jwt(jwt -> jwt
        // .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Apply JWT
        // authentication converter
        // )
        // );

        // return http.build();
    }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {
    //     JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    //     // Customize converter if necessary
    //     return converter;
    // }
}
