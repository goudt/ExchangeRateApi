package com.betvictor.exchangerateapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class RequestCurrencyList {
    List<String> currencyList;
}
