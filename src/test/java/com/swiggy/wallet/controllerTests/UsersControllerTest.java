package com.swiggy.wallet.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.execptions.UserAlreadyExistsException;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.responseModels.UserResponseModel;
import com.swiggy.wallet.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(userService);
    }

    @Test
    void expectUserCreated() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("user", "password");
        UserResponseModel userResponseModel = new UserResponseModel("user", new Wallet());

        when(userService.register(userRequestModel)).thenReturn(userResponseModel);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).register(userRequestModel);
    }

    @Test
    void expectUserAlreadyExists() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("user","password");

        when(userService.register(userRequestModel)).thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).register(userRequestModel);
    }

    @Test
    @WithMockUser(username = "user")
    void expectUserDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete();
    }
}
