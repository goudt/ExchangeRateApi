package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.service.ExchangeRateService;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/{fromCurrency}/{fromAmmount}/{toCurrency}")
    @ResponseStatus(HttpStatus.OK)
    public String getExchangeRate(@PathVariable Double fromAmmount,
                                  @PathVariable String fromCurrency,
                                  @PathVariable String toCurrency){
        //String latestExchangeRates = exchangeRateService.getLatestExchangeRates();
        String latestExchangeRates = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        System.out.println(latestExchangeRates);
        return latestExchangeRates;
    }


    /*
    a. Get exchange rate from Currency A to Currency B
    b. Get all exchange rates from Currency A
    c. Get value conversion from Currency A to Currency B
    d. Get value conversion from Currency A to a list of supplied currencies
 */
}
