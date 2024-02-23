package com.swiggy.wallet.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.SameUserTransactionException;
import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.services.TransactionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(transactionService);
    }

    @Test
    @WithMockUser(username = "sender")
    void expectTransactionSuccessful() throws Exception {
        TransactionRequestModel transactionRequestModel = new TransactionRequestModel("sender", 1, 2, new Money(100, Currency.RUPEE));
        String requestJson = objectMapper.writeValueAsString(transactionRequestModel);

        mockMvc.perform(put("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).transaction(transactionRequestModel);
    }

    @Test
    @WithMockUser(username = "sender")
    void expectReceiverNotFoundInTransaction() throws Exception {
        TransactionRequestModel transactionRequestModel = new TransactionRequestModel("sender", 1, 2, new Money(100, Currency.RUPEE));
        String requestJson = objectMapper.writeValueAsString(transactionRequestModel);

        when(transactionService.transaction(transactionRequestModel)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
        verify(transactionService, times(1)).transaction(transactionRequestModel);
    }

    @Test
    @WithMockUser(username = "sender")
    void expectSameUserCanNotDoTransaction() throws Exception {
        TransactionRequestModel transactionRequestModel = new TransactionRequestModel("sender", 1, 2, new Money(100, Currency.RUPEE));
        String requestJson = objectMapper.writeValueAsString(transactionRequestModel);

        when(transactionService.transaction(transactionRequestModel)).thenThrow(SameUserTransactionException.class);

        mockMvc.perform(put("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
        verify(transactionService, times(1)).transaction(transactionRequestModel);
    }

    @Test
    @WithMockUser(username = "user")
    void expectFetchAllTransaction() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).fetchTransactions(any(), any());
    }

    @Test
    void expectFetchFromUnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void expectFetchAllTransactionWithDate() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .param("fromDateTime", "2024-02-19T15:06:42.598459")
                        .param("toDateTime", "2024-02-19T15:06:50.598459")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).fetchTransactions(any(), any());
    }
}
