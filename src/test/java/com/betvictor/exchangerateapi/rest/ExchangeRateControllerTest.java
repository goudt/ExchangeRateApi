package com.betvictor.exchangerateapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExchangeRateControllerTest extends AbstractControllerTest{

    @Test
    public void shouldReturnLatestCurrencyConversions() throws Exception {
  /*
        // given


        // when
        ObjectMapper mapper = new ObjectMapper();
        String euroRates = mapper.writeValueAsString(mapper.readValue(new File("src/test/java/com/betvictor/exchangerateapi/rest/EuroRates.json"), Object.class));
        when(exchangeRateService.retreiveAllRatesFrom3rdParty("EUR")).thenReturn(euroRates);

        // then
        mockMvc.perform(get("/api/EUR/USD").accept(APPLICATION_JSON_VALUE).contentType(APPLICATION_JSON_VALUE).characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//                .andExpect(jsonPath("$", hasSize(1)));
//                .andExpect(content().contentTypeCompatibleWith("application/json"));
//                .andExpect(content().contentType(APPLICATION_JSON));
//                .andExpect(jsonPath("$.from", is("EUR")))
//                .andExpect(jsonPath("$.to", is("USD")))

*/
    }



}
