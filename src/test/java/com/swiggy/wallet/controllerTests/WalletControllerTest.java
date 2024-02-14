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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @WithMockUser(roles = "USER")
    void deposit_withValidAmount() throws Exception {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(put("/api/wallet/{walletId}/deposit", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isOk());

        verify(walletService, times(1)).deposit(anyLong(), any(WalletRequestModel.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void withdraw_withValidAmount() throws Exception {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(put("/api/wallet/{walletId}/withdraw", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        verify(walletService, times(1)).withdraw(anyLong(), any(WalletRequestModel.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_validWallet() throws Exception {
        mockMvc.perform(post("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(walletService, times(1)).create();
    }

    @Test
    @WithMockUser(roles = "USER")
    void get_validWallet() throws Exception {
        long walletId = 1;
        mockMvc.perform(post("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/wallet/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(walletService, times(1)).checkBalance(anyLong());
    }
}
