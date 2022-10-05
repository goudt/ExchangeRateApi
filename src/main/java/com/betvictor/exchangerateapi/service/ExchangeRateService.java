package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    private final CacheService cacheService;

    public ExchangeRateService(RestTemplateBuilder builder, InMemoryCacheService cacheService) {
        this.restTemplate = builder.build();
        this.cacheService = cacheService;
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
        return exchangeRate.orElseThrow();
    }

    public List<ExchangeRate> getAllExchangeRates(String fromCurrency) {
        String resultStr = retreiveAllRatesFrom3rdParty(fromCurrency);
        JSONObject currencyRates = new JSONObject(resultStr).getJSONObject("rates");

        return currencyRates.keySet().stream()
                .filter(c -> c != fromCurrency)
                .map(c -> ExchangeRate.builder()
                        .from(fromCurrency)
                        .to(c)
                        .ratio(currencyRates.getDouble(c))
                        .date(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ExchangeRate> getExchangeRates(String fromCurrency, List<String> toCurrencyLst) {
        String resultStr = retreiveAllRatesFrom3rdParty(fromCurrency);
        JSONObject currencyRates = new JSONObject(resultStr).getJSONObject("rates");

        return currencyRates.keySet().stream()
                .filter(c -> c != fromCurrency)
                .filter(c -> toCurrencyLst.contains(c))
                .map(c -> ExchangeRate.builder()
                        .from(fromCurrency)
                        .to(c)
                        .ratio(currencyRates.getDouble(c))
                        .date(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }

    public String retreiveAllRatesFrom3rdParty(String fromCurrency) {
        URI targetUrl = UriComponentsBuilder.fromUriString("https://api.exchangerate.host/")
                .path("/latest")
                .queryParam("base", fromCurrency)
                .build()
                .encode()
                .toUri();
        String resultStr = restTemplate.getForObject(targetUrl, String.class);

        return resultStr;
    }
}
