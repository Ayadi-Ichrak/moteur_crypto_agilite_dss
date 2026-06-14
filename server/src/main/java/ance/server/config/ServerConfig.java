package ance.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration du serveur ANCE
 */
@Configuration
public class ServerConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
