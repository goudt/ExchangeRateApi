package com.betvictor.exchangerateapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CacheInputDto {
    private final String fromCurrency;
    private final String toCurrency;
}
