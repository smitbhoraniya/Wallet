package com.swiggy.wallet.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.services.WalletService;
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

    @Test
    @WithMockUser(username = "user")
    void expectDepositMoney() throws Exception {
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(put("/api/v1/wallets/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(walletService, times(1)).deposit(anyInt(), anyString(), any(WalletRequestModel.class));
    }

    @Test
    void unauthorizedOnDeposit() throws Exception {
        WalletRequestModel requestModel = new WalletRequestModel(100.0, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(put("/api/v1/wallets/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        verify(walletService, never()).deposit(anyInt(), anyString(), any());
    }

    @Test
    @WithMockUser(username = "user")
    void expectWithdrawMoney() throws Exception {
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(put("/api/v1/wallets/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        verify(walletService, times(1)).withdraw(anyInt(), anyString(), any(WalletRequestModel.class));
    }

    @Test
    void unauthorizedOnWithdraw() throws Exception {
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);

        String requestBody = objectMapper.writeValueAsString(requestModel);
        mockMvc.perform(put("/api/v1/wallets/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        verify(walletService, never()).withdraw(anyInt(), anyString(), any());
    }

    @Test
    @WithMockUser(username = "user")
    void expectGetAllWallets() throws Exception {
        mockMvc.perform(get("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(walletService, times(1)).fetchWallets();
    }
}
