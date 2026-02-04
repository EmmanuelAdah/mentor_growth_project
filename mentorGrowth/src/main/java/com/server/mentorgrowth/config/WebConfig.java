package com.server.mentorgrowth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

//    @Bean
//    public WebMvcConfigurer corsRegistry(CorsRegistry registry) {
//        return registry.addMapping("/api/v1/**")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//                .allowedHeaders(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .allowedOrigins("")
//                .allowCredentials(true);
//    }

}
