package com.swiggy.wallet.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        reset(walletService);
    }

    @Test
    void deposit_withValidAmount() throws Exception {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(post("/api/wallet/{walletId}/deposit", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isOk());
    }

    @Test
    void withdraw_withValidAmount() throws Exception {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(post("/api/wallet/{walletId}/withdraw", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}
