package com.betvictor.exchangerateapi.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExchangeRateControllerTest extends AbstractControllerTest{

    @Test
    public void shouldReturnLatestCurrencyConvertions() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(get("/api/EUR/1.234/USD").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
//                .andExpect(jsonPath("$.title", is("Title")));

    }
}
