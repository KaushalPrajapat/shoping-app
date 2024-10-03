package com.shoping.payment_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shoping.payment_service.external.decoder.CustomErrorDecoder;


@Configuration
public class FeignConfig {
    @Bean
    CustomErrorDecoder errorDecoders() {
        return new CustomErrorDecoder();
    }

}