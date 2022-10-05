package com.betvictor.exchangerateapi.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    public ExchangeRateService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public String getLatestExchangeRates(){
        return restTemplate.getForObject("https://api.exchangerate.host/latest", String.class);
    }

    public String getExchangeRate(String fromCurrency, String toCurrency){
        URI targetUrl= UriComponentsBuilder.fromUriString("https://api.exchangerate.host/")  // Build the base link
                .path("/convert")                            // Add path
                .queryParam("from", fromCurrency)
                .queryParam("to", toCurrency)// Add one or more query params
                .build()                                                 // Build the URL
                .encode()                                                // Encode any URI items that need to be encoded
                .toUri();
        return restTemplate.getForObject(targetUrl, String.class);
    }

//    https://api.exchangerate.host/convert?from=USD&to=EUR


//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }

//    @Bean
//    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//        return args -> {
//            String response = restTemplate.getForObject(
//                    "https://api.exchangerate.host/latest", String.class);
//            log.info(response);
//        };
//    }

}
