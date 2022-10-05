package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.model.ExchangeRateDto;
import com.betvictor.exchangerateapi.model.RequestCurrencyList;
import com.betvictor.exchangerateapi.service.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/api")

public class ExchangeRateController {


    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping(value = "/{fromCurrency}/{toCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExchangeRateDto> getExchangedAmount(@PathVariable String fromCurrency,
                                                              @PathVariable String toCurrency,
                                                              @RequestParam(required = false) Double fromAmount) {
        ExchangeRate latestExchangeRate;
        try {
            latestExchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        } catch (Exception e) {
            return new ResponseEntity("Unable to retrieve data from 3rd party server",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(toCurrency)
                .amount(fromAmount !=null ? fromAmount : 1l)
                .result(latestExchangeRate.getRatio() * (fromAmount != null ? fromAmount : 1l))
                .build(), HttpStatus.OK);
    }

    @GetMapping(value = "/{fromCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ExchangeRateDto>> getAllExchangeRates(@PathVariable String fromCurrency) {

        List<ExchangeRate> latestExchangeRates;
        try {
            latestExchangeRates = exchangeRateService.getAllExchangeRates(fromCurrency);
        } catch (Exception e) {
            return new ResponseEntity("Unable to retrieve data from 3rd party server",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(latestExchangeRates.stream().map(r -> ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(r.getTo())
                .amount((double)1l)
                .result(r.getRatio())
                .build()).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(value = "/{fromCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ExchangeRateDto>>  getSelectedExchangeRates(@PathVariable String fromCurrency,
                                                                           @RequestBody RequestCurrencyList toCurrencyLst) {
        List<ExchangeRate> latestExchangeRates;
        try {
            latestExchangeRates = exchangeRateService.getExchangeRates(fromCurrency, toCurrencyLst.getCurrencyList());
        } catch (Exception e) {
            return new ResponseEntity("Unable to retrieve data from 3rd party server",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(latestExchangeRates.stream().map(r -> ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(r.getTo())
                .amount((double)1l)
                .result(r.getRatio())
                .build()).collect(Collectors.toList()), HttpStatus.OK);
    }

}
