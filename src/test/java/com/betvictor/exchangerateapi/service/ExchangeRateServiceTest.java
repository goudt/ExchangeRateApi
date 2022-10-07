package com.betvictor.exchangerateapi.service;

import com.betvictor.exchangerateapi.AbstractTest;
import com.betvictor.exchangerateapi.model.ExchangeRate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExchangeRateServiceTest extends AbstractTest {
    private final ExchangeRateService service;

    public ExchangeRateServiceTest() {
        this.service = new ExchangeRateService(new InMemoryCacheService(), exchangeRateHostApiClient);
    }

    @Test
    void getExchangeRate() {
    }

    @Test
    void getAllExchangeRates() throws IOException {
    }

    @Test
    void getExchangeRates() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String euroRates = mapper.writeValueAsString(mapper.readValue(new File("src/test/resources/EURExchangeRates.json"), Object.class));

        // given
        List<String> inCurrencies = List.of("USD", "JPY");

        // when
        when(exchangeRateHostApiClient.retreiveAllRatesFrom3rdParty("EUR"))
                .thenReturn(euroRates);

        // then
        List<ExchangeRate> eurRates = service.getExchangeRates("EUR", inCurrencies);
        assertNotNull(eurRates);

    }
}