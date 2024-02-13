package com.swiggy.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.models.ErrorResponse;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        reset(walletRepository);
    }

    @Test
    void deposit_withValidAmount() throws Exception {
        long walletId = 1;
        int depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        String requestBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = mockMvc.perform(post("/api/wallet/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        WalletResponseModel actualResponseModel = objectMapper.readValue(responseBody, WalletResponseModel.class);

        WalletResponseModel responseModel = new WalletResponseModel(150);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(150, wallet.getMoney());
    }

    @Test
    void deposit_withNegativeAmount_shouldThrowInvalidMoneyException() throws Exception {
        long walletId = 1;
        int depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        String requestBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = mockMvc.perform(post("/api/wallet/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse actualResponseModel = objectMapper.readValue(responseBody, ErrorResponse.class);

        ErrorResponse responseModel = new ErrorResponse(400, "Money should be positive.", System.currentTimeMillis());
        assertEquals(responseModel.getMessage(), actualResponseModel.getMessage());
        assertEquals(100, wallet.getMoney());
    }

    @Test
    void withdraw_withValidAmount() throws Exception {
        long walletId = 1;
        int depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        String requestBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = mockMvc.perform(post("/api/wallet/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        WalletResponseModel actualResponseModel = objectMapper.readValue(responseBody, WalletResponseModel.class);

        WalletResponseModel responseModel = new WalletResponseModel(50);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(50, wallet.getMoney());
    }

    @Test
    void withdraw_withNegativeAmount_shouldThrowInvalidMoneyException() throws Exception {
        long walletId = 1;
        int depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        String requestBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = mockMvc.perform(post("/api/wallet/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse actualResponseModel = objectMapper.readValue(responseBody, ErrorResponse.class);

        ErrorResponse responseModel = new ErrorResponse(400, "Money should be positive.", System.currentTimeMillis());
        assertEquals(responseModel.getMessage(), actualResponseModel.getMessage());
        assertEquals(100, wallet.getMoney());

    }

    @Test
    void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() throws Exception {
        long walletId = 1;
        int depositMoney = 200;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        String requestBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = mockMvc.perform(post("/api/wallet/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse actualResponseModel = objectMapper.readValue(responseBody, ErrorResponse.class);

        ErrorResponse responseModel = new ErrorResponse(400, "Don't have enough money.", System.currentTimeMillis());
        assertEquals(responseModel.getMessage(), actualResponseModel.getMessage());
        assertEquals(100, wallet.getMoney());
    }
}
