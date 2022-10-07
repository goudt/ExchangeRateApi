package com.betvictor.exchangerateapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ExchangeRateApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateApiApplication.class, args);
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }
}
