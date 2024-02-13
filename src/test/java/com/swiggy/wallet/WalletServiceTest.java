package com.swiggy.wallet;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void deposit_withValidAmount() throws Exception {
        String depositRequestBody = "{\"money\": 100}";
        mockMvc.perform(post("/api/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositRequestBody))
                .andExpect(status().isOk());
    }

    @Test
    void deposit_withNegativeAmount_shouldThrowIllegalArgumentException() throws Exception {
        String requestBody = "{\"money\": -50}";
        mockMvc.perform(post("/api/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_withValidAmount() throws Exception {
        String depositRequestBody = "{\"money\": 100}";
        mockMvc.perform(post("/api/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositRequestBody))
                .andExpect(status().isOk());

        String requestBody = "{\"money\": 50}";
        mockMvc.perform(post("/api/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.money").value(50));
    }

    @Test
    void withdraw_withNegativeAmount_shouldThrowIllegalArgumentException() throws Exception {
        String requestBody = "{\"money\": -50}";
        mockMvc.perform(post("/api/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_withInsufficientFunds_shouldThrowUnsupportedOperationException() throws Exception {
        String requestBody = "{\"money\": 50}";
        mockMvc.perform(post("/api/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
