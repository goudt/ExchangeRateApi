package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.CacheInputDto;
import com.betvictor.exchangerateapi.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InMemoryCacheService extends CacheService {
    public static final String KEY_PATTERN_STR = "%s->%s";
    private static ConcurrentHashMap<String, ExchangeRate> cacheMap = new ConcurrentHashMap<>();

    @Value("${cache.duration.in.minutes}")
    private int cacheDuration;

    @Value("#{'${cache.currency.codes}'.split(',')}")
    private List<String> currencyCodes;

    @Override
    Optional<ExchangeRate> retrieveRate(CacheInputDto cacheInputDto) {
        ExchangeRate rate = cacheMap.get(String.format(KEY_PATTERN_STR, cacheInputDto.getFromCurrency(),
                cacheInputDto.getToCurrency()));
        long timespam = cacheDuration;

        if (rate != null) {
            timespam = Math.abs(ChronoUnit.MINUTES.between(LocalDateTime.now(), rate.getDate()));
            if (timespam < cacheDuration) {
                System.out.println(String.format("Retrieved %s from cache", rate));
            }
        }

        return (rate != null && timespam < cacheDuration
                ? Optional.of(rate) : Optional.empty());
    }

    @Override
    void storeRate(ExchangeRate exchangeRate) {
        cacheMap.put(String.format(KEY_PATTERN_STR, exchangeRate.getFrom(), exchangeRate.getTo()), exchangeRate);
        cacheMap.put(String.format(KEY_PATTERN_STR, exchangeRate.getTo(), exchangeRate.getFrom()), exchangeRate.invert());
    }

}
