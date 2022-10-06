package com.betvictor.exchangerateapi.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ExchangeRateHostApiClient {
    private final RestTemplate restTemplate;

    public String retreiveAllRatesFrom3rdParty(String fromCurrency) {
        URI targetUrl = UriComponentsBuilder.fromUriString("https://api.exchangerate.host/")
                .path("/latest")
                .queryParam("base", fromCurrency)
                .build()
                .encode()
                .toUri();

        return restTemplate.getForObject(targetUrl, String.class);
    }


    public String retreiveRateFrom3rdParty(String fromCurrency, String toCurrency) {
        URI targetUrl = UriComponentsBuilder.fromUriString("https://api.exchangerate.host/")
                .path("/convert")
                .queryParam("from", fromCurrency)
                .queryParam("to", toCurrency)
                .build()
                .encode()
                .toUri();

        return restTemplate.getForObject(targetUrl, String.class);
    }

}