package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.CacheInputDto;
import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.model.ExchangeRateDto;
import com.betvictor.exchangerateapi.rest.ExchangeRateHostApiClient;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {

    private final CacheService cacheService;

    private final ExchangeRateHostApiClient apiClient;

    public ExchangeRateService(InMemoryCacheService cacheService,
                               ExchangeRateHostApiClient apiClient) {
        this.cacheService = cacheService;
        this.apiClient = apiClient;
    }

    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> exchangeRate = cacheService.retrieveRate(new CacheInputDto(fromCurrency, toCurrency))
                .or(() -> {
                    JSONObject resultJson = new JSONObject(apiClient.retreiveRateFrom3rdParty(fromCurrency, toCurrency));
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
        String resultStr = apiClient.retreiveAllRatesFrom3rdParty(fromCurrency);
        JSONObject currencyRates = new JSONObject(resultStr).getJSONObject("rates");

        return currencyRates.keySet().stream()
                .filter(c -> !c.equals(fromCurrency))
                .map(c -> ExchangeRate.builder()
                        .from(fromCurrency)
                        .to(c)
                        .ratio(currencyRates.getDouble(c))
                        .date(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ExchangeRate> getExchangeRates(String fromCurrency, List<String> toCurrencyLst) {
        String resultStr = apiClient.retreiveAllRatesFrom3rdParty(fromCurrency);
        JSONObject currencyRates = new JSONObject(resultStr).getJSONObject("rates");

        var requiredRates = toCurrencyLst.stream()
                .map(cur->new CacheInputDto(fromCurrency,cur))
                .collect(Collectors.toList());
        List<Optional<ExchangeRate>> cachedRates = cacheService.retrieveRatesList(requiredRates);

        if (cachedRates.size() < cachedRates.size()){
            System.out.println("all rates must be retrieved ");
            // not all rates are cached, we should retrieve them
            List<ExchangeRate> exchangeRates = currencyRates.keySet().stream()
                    .filter(c -> !c.equals(fromCurrency))
                    .filter(toCurrencyLst::contains)
                    .map(c -> ExchangeRate.builder()
                            .from(fromCurrency)
                            .to(c)
                            .ratio(currencyRates.getDouble(c))
                            .date(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            cacheService.storeRateList(exchangeRates);

            return exchangeRates;
        } else {
            // all rates retrieved from cache
            System.out.println("all rates retrieved from cache");

            return cachedRates.stream().map(er -> er.orElseThrow()).collect(Collectors.toList());
        }
    }

}
