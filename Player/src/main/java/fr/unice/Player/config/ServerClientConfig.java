package fr.unice.Player.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServerClientConfig {

    @Value("${game.host}")
    private String hostname;

    @Value("${game.port}")
    private Integer port;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("http://" + hostname + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
