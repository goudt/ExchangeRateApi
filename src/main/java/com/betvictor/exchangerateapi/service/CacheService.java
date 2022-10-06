package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.model.CacheInputDto;
import com.betvictor.exchangerateapi.model.ExchangeRate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CacheService {
    abstract Optional<ExchangeRate> retrieveRate(CacheInputDto inputDto);
    abstract void storeRate(ExchangeRate exchangeRate);

    public List<Optional<ExchangeRate>> retrieveRatesList (List<CacheInputDto> inputDto){
        return inputDto.stream().map(this::retrieveRate).collect(Collectors.toList());
    }
    void storeRateList(List<ExchangeRate> exchangeRates){
        Thread storingThread = new Thread(()->{
            exchangeRates.forEach(er -> storeRate(er));
        });
        storingThread.run();
    }
}
