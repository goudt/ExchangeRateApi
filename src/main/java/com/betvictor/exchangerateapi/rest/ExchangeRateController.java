package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.model.ExchangeRateDto;
import com.betvictor.exchangerateapi.model.RequestCurrencyList;
import com.betvictor.exchangerateapi.model.ResponseCurrencyList;
import com.betvictor.exchangerateapi.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/api")

public class ExchangeRateController {

    @Value("#{'${cache.currency.codes}'.split(',')}")
    private List<String> currencyCodes;
    private static final String UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER =
            "Unable to retrieve data from 3rd party server";
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping(value = "/{fromCurrency}/{toCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExchangeRateDto> getExchangedAmount(@PathVariable String fromCurrency,
                                                              @PathVariable String toCurrency,
                                                              @RequestParam(required = false) Double fromAmount) {
        validateCurrencyCodes(List.of(fromCurrency, toCurrency));

        ExchangeRate latestExchangeRate;
        try {
            latestExchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER);
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
    public ResponseEntity<ResponseCurrencyList> getAllExchangeRates(@PathVariable String fromCurrency) {

        validateCurrencyCodes(List.of(fromCurrency));

        List<ExchangeRate> latestExchangeRates;
        try {
            latestExchangeRates = exchangeRateService.getAllExchangeRates(fromCurrency);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER);
        }
        ResponseCurrencyList result = new ResponseCurrencyList();
        result.setRates(latestExchangeRates.stream().map(r -> ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(r.getTo())
                .amount((double)1l)
                .result(r.getRatio())
                .build()).collect(Collectors.toList()));

        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @PostMapping(value = "/{fromCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseCurrencyList>  getSelectedExchangeRates(@PathVariable String fromCurrency,
                                                                           @RequestBody RequestCurrencyList toCurrencyLst) {
        List<String> codesInput = List.of(fromCurrency);
        codesInput.addAll(toCurrencyLst.getCurrencyList());
        validateCurrencyCodes(codesInput);

        List<ExchangeRate> latestExchangeRates;
        try {
            latestExchangeRates = exchangeRateService.getExchangeRates(fromCurrency, toCurrencyLst.getCurrencyList());
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER);
        }
        ResponseCurrencyList result = new ResponseCurrencyList();
        result.setRates(latestExchangeRates.stream().map(r -> ExchangeRateDto.builder()
                .from(fromCurrency)
                .to(r.getTo())
                .amount((double)1l)
                .result(r.getRatio())
                .build()).collect(Collectors.toList()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void validateCurrencyCodes(List<String> codes){
        codes.forEach(c-> {
            if (!currencyCodes.contains(c))
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                        String.format("%s is not a valid currency code!", c));});
    }
}
