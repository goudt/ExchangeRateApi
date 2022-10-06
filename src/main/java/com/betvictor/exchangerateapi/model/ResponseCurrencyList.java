package com.betvictor.exchangerateapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class ResponseCurrencyList {
    private List<ExchangeRateDto> rates;
}
