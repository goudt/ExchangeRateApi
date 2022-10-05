package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.model.ExchangeRateDto;
import com.betvictor.exchangerateapi.service.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api")

public class ExchangeRateController {


    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping(value = "/{fromCurrency}/{fromAmount}/{toCurrency}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExchangeRateDto> getExchangedAmount(@PathVariable String fromCurrency,
                                                           @PathVariable Double fromAmount,
                                                           @PathVariable String toCurrency){
        //String latestExchangeRates = exchangeRateService.getLatestExchangeRates();
        ExchangeRate latestExchangeRates = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return new ResponseEntity<>(ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(toCurrency)
                .amount(fromAmount)
                .result(latestExchangeRates.getRatio() * fromAmount)
                .build(), HttpStatus.OK);
    }

    @GetMapping(value = "/{fromCurrency}/{toCurrency}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExchangeRateDto> getExchangeRate(@PathVariable String fromCurrency,
                                                           @PathVariable String toCurrency){
        ExchangeRate latestExchangeRates = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return new ResponseEntity<>(ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(toCurrency)
                .amount((double) 1l)
                .result(latestExchangeRates.getRatio())
                .build(), HttpStatus.OK);
    }


    /*
    a. Get exchange rate from Currency A to Currency B
    b. Get all exchange rates from Currency A
    c. Get value conversion from Currency A to Currency B
    d. Get value conversion from Currency A to a list of supplied currencies
 */
}
