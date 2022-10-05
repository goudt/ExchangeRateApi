package com.betvictor.exchangerateapi.rest;

import com.betvictor.exchangerateapi.service.ExchangeRateService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
public class AbstractControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @MockBean
    protected ExchangeRateService exchangeRateService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(exchangeRateService);
    }
}
