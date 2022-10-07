package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.betvictor.exchangerateapi.model.ExchangeRateDto;
import com.betvictor.exchangerateapi.model.RequestCurrencyList;
import com.betvictor.exchangerateapi.model.ResponseCurrencyList;
import com.betvictor.exchangerateapi.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ExchangeRateController {

    @Value("#{'${cache.currency.codes}'.split(',')}")
    private List<String> currencyCodes;
    private static final String UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER =
            "Unable to retrieve data from 3rd party server";
    private final ExchangeRateService exchangeRateService;


    @Operation(summary = "Get single exchange rate", description = "Get the exchange rate for 2 currency codes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "400", description = "Provided currency code(s) are invalid"),
        @ApiResponse(responseCode = "503", description = "Unable to retrieve rates from external server") })
    @GetMapping(value = "/{fromCurrency}/{toCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExchangeRateDto> getExchangedAmount(@PathVariable String fromCurrency,
                                                              @PathVariable String toCurrency,
                                                              @RequestParam(required = false) Double amount) {
        validateCurrencyCodes(List.of(fromCurrency, toCurrency));

        try {
            ExchangeRate latestExchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

            return new ResponseEntity<>(ExchangeRateDto.builder()
                    .from(fromCurrency)
                    .to(toCurrency)
                    .amount(amount != null ? amount : 1l)
                    .result(latestExchangeRate.getRatio() * (amount != null ? amount : 1l))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER);
        }


    }

    @Operation(summary = "Get all exchange exchange rates", description = "Get the exchange rates for currency code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Provided currency code(s) are invalid"),
            @ApiResponse(responseCode = "503", description = "Unable to retrieve rates from external server") })    @GetMapping(value = "/{fromCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseCurrencyList> getAllExchangeRates(@PathVariable String fromCurrency) {

        validateCurrencyCodes(List.of(fromCurrency));

        try {
            List<ExchangeRate> latestExchangeRates = exchangeRateService.getAllExchangeRates(fromCurrency);

            ResponseCurrencyList result = new ResponseCurrencyList();
            result.setRates(latestExchangeRates.stream().map(r -> ExchangeRateDto.builder()
                    .from(fromCurrency)
                    .to(r.getTo())
                    .amount((double) 1l)
                    .result(r.getRatio())
                    .build()).collect(Collectors.toList()));

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER);
        }
    }

    @Operation(summary = "Get selected exchange rates", description = "Get the selected exchange rates for currency code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Provided currency code(s) are invalid"),
            @ApiResponse(responseCode = "503", description = "Unable to retrieve rates from external server") })
    @PostMapping(value = "/{fromCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseCurrencyList> getSelectedExchangeRates(
            @PathVariable String fromCurrency,
            @RequestBody RequestCurrencyList toCurrencyLst) {

        List<String> codesInput = List.of(fromCurrency);
        codesInput.addAll(toCurrencyLst.getCurrencyList());
        validateCurrencyCodes(codesInput);

        try {
            List<ExchangeRate> latestExchangeRates = exchangeRateService
                    .getExchangeRates(fromCurrency, toCurrencyLst.getCurrencyList());

            ResponseCurrencyList result = new ResponseCurrencyList();
            result.setRates(latestExchangeRates.stream().map(r -> ExchangeRateDto.builder()
                    .from(fromCurrency)
                    .to(r.getTo())
                    .amount((double) 1l)
                    .result(r.getRatio())
                    .build()).collect(Collectors.toList()));

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    UNABLE_TO_RETRIEVE_DATA_FROM_3_RD_PARTY_SERVER);
        }
    }

    private void validateCurrencyCodes(List<String> codes) {
        codes.forEach(c -> {
            if (!currencyCodes.contains(c))
                throw new InputValidationException(String.format("%s is not a valid currency code!", c));
        });
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    private class InputValidationException extends RuntimeException {
        InputValidationException(String message) {
            super(message);
        }
    }

}
