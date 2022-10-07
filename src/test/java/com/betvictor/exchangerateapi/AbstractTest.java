package com.betvictor.exchangerateapi;

import com.betvictor.exchangerateapi.rest.ExchangeRateHostApiClient;
import com.betvictor.exchangerateapi.service.ExchangeRateService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
public class AbstractTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @MockBean
    protected ExchangeRateHostApiClient exchangeRateHostApiClient;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(exchangeRateHostApiClient);
    }
}
