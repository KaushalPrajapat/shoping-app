package errorcheck.error.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import errorcheck.error.external.CustomErrorDecoder;

@Configuration
public class FeignConfig {
    @Bean
    CustomErrorDecoder errorDecoders(){
        return new CustomErrorDecoder();
    }
    
}