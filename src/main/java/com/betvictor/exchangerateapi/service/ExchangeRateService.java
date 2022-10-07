package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.CacheInputDto;
import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.rest.ExchangeRateHostApiClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);
    @Value("#{'${cache.currency.codes}'.split(',')}")
    private List<String> currencyCodes;
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
                    JSONObject resultJson = new JSONObject(apiClient.retrieveRateFrom3rdParty(fromCurrency, toCurrency));
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
        List<Optional<ExchangeRate>> cachedRates = getCachedRates(fromCurrency, currencyCodes);

        if (cachedRates.size() < currencyCodes.size() - 1) {
            String resultStr = apiClient.retreiveAllRatesFrom3rdParty(fromCurrency);
            JSONObject currencyRates = new JSONObject(resultStr).getJSONObject("rates");

            List<ExchangeRate> exchangeRates = currencyRates.keySet().stream()
                    .filter(c -> !c.equals(fromCurrency))
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
            log.info("all rates retrieved from cache");

            return cachedRates.stream().map(Optional::orElseThrow).collect(Collectors.toList());
        }

    }

    public List<ExchangeRate> getExchangeRates(String fromCurrency, List<String> toCurrencyLst) {

        List<Optional<ExchangeRate>> cachedRates = getCachedRates(fromCurrency, toCurrencyLst);

        if (cachedRates.size() < toCurrencyLst.size() - 1) {
            String resultStr = apiClient.retreiveAllRatesFrom3rdParty(fromCurrency);
            JSONObject currencyRates = new JSONObject(resultStr).getJSONObject("rates");

            log.info("all rates must be retrieved ");
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
            log.info("all rates retrieved from cache");

            return cachedRates.stream().map(Optional::orElseThrow).collect(Collectors.toList());
        }
    }

    private List<Optional<ExchangeRate>> getCachedRates(String fromCurrency, List<String> toCurrencyLst) {
        var requiredRates = toCurrencyLst.stream()
                .map(cur -> new CacheInputDto(fromCurrency, cur))
                .collect(Collectors.toList());
        List<Optional<ExchangeRate>> cachedRates = cacheService.retrieveRatesList(requiredRates);
        cachedRates = cachedRates.stream().filter(Optional::isPresent).collect(Collectors.toList());
        return cachedRates;
    }

}
