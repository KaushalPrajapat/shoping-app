package com.shoping.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

    // @Bean
    // Customizer<Resilience4JCircuitBreakerFactory> defaulCustomizer() {
	// 	return factory -> factory.configureDefault(
	// 			id -> new Resilience4JConfigBuilder(id)
	// 					.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build());
	// }

}
