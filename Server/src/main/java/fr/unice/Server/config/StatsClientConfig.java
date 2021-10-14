package fr.unice.Server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableAsync
public class StatsClientConfig {

    @Value("${stats.host}")
    private String hostname;

    @Value("${stats.port}")
    private Integer port;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(String.format("http://%s:%d", hostname, port))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
