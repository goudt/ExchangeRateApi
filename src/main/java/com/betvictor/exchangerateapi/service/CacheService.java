package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public abstract class CacheService {
    abstract Optional<ExchangeRate> retrieveRate(String from, String to);
    abstract List<ExchangeRate> retrieveRates(String from);
    abstract void storeRate(ExchangeRate exchangeRate);
}
