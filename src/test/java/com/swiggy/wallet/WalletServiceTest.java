package com.swiggy.wallet;

import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.services.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WalletServiceTest {
    private WalletService walletService;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        walletService = new WalletService(wallet);
    }

    @Test
    void withdraw_withValidAmount() {
        WalletRequestModel requestModel = new WalletRequestModel(10);
        WalletResponseModel expectedResponse = new WalletResponseModel(0);

        walletService.deposit(new WalletRequestModel(10));
        WalletResponseModel actualResponse = walletService.withdraw(requestModel);

        assertEquals(expectedResponse.getMoney(), actualResponse.getMoney());
        assertEquals(0, wallet.getMoney());
    }

    @Test
    void withdraw_withNegativeAmount_shouldThrowIllegalArgumentException() {
        WalletRequestModel requestModel = new WalletRequestModel(-50);

        assertThrows(IllegalArgumentException.class, () -> {
            walletService.withdraw(requestModel);
        });
    }

    @Test
    void withdraw_withInsufficientFunds_shouldThrowUnsupportedOperationException() {
        WalletRequestModel requestModel = new WalletRequestModel(50);

        assertThrows(UnsupportedOperationException.class, () -> {
            walletService.withdraw(requestModel);
        });
    }

    @Test
    void deposit_withValidAmount() {
        WalletRequestModel requestModel = new WalletRequestModel(50);
        WalletResponseModel expectedResponse = new WalletResponseModel(50);

        WalletResponseModel actualResponse = walletService.deposit(requestModel);

        assertEquals(expectedResponse.getMoney(), actualResponse.getMoney());
        assertEquals(50, wallet.getMoney());
    }

    @Test
    void deposit_withNegativeAmount_shouldThrowIllegalArgumentException() {
        WalletRequestModel requestModel = new WalletRequestModel(-50);

        assertThrows(IllegalArgumentException.class, () -> {
            walletService.deposit(requestModel);
        });
    }
}
