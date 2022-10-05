package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import java.util.Optional;

abstract public class CacheService {
    abstract Optional<ExchangeRate> retrieveRate(String from, String to);
    abstract void storeRate(ExchangeRate exchangeRate);
}
