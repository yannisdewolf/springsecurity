package be.dewolf.hofleverancier.ordertaking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@RestController
public class OrdertakingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdertakingApplication.class, args);
    }

    @GetMapping("/basicinfo")
    public String basicInfo() {
        return "Allowed for everybody, even non-authorized";
    }

    @GetMapping("/publicdisclaimer")
    public String publicdisclaimer() {
        return "Disclaimer Allowed for everybody, even non-authorized";
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

