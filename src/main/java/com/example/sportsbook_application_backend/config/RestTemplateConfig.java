package com.example.sportsbook_application_backend.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;


@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler("https://api-football-v1.p.rapidapi.com/v3/");
        return builder
                .uriTemplateHandler(uriTemplateHandler)
                .defaultHeader("X-RapidAPI-Key","2abe9f6666msh12e81b74ce79b38p1f70a7jsnc9695ddd515a")
                .defaultHeader("X-RapidAPI-Host","api-football-v1.p.rapidapi.com")
                .build();
    }
}
