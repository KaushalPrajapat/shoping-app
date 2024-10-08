package errorcheck.error;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ErrorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErrorApplication.class, args);
	}

}
