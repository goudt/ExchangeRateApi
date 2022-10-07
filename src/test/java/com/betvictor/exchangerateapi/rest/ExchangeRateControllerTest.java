package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.AbstractTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class ExchangeRateControllerTest extends AbstractTest {

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
