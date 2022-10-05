package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.model.ExchangeRateDto;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    private final CacheService cacheService;

    public ExchangeRateService(RestTemplateBuilder builder, InMemoryCacheService cacheService) {
        this.restTemplate = builder.build();
        this.cacheService = cacheService;
    }


    public String getLatestExchangeRates() {
        return restTemplate.getForObject("https://api.exchangerate.host/latest", String.class);
    }

    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> exchangeRate = cacheService.retrieveRate(fromCurrency, toCurrency)
                .or(() -> {
                    URI targetUrl = UriComponentsBuilder.fromUriString("https://api.exchangerate.host/")
                            .path("/convert")
                            .queryParam("from", fromCurrency)
                            .queryParam("to", toCurrency)
                            .build()
                            .encode()
                            .toUri();
                    String resultStr = restTemplate.getForObject(targetUrl, String.class);
                    JSONObject resultJson = new JSONObject(resultStr);
                    ExchangeRate result = ExchangeRate.builder()
                            .from(fromCurrency)
                            .to(toCurrency)
                            .ratio(resultJson.getJSONObject("info").getDouble("rate"))
                            .date(LocalDateTime.now())
                            .build();

                        cacheService.storeRate(result);

                    return Optional.of(result);
                });
        return exchangeRate.orElseThrow(); //fixme
    }

}
