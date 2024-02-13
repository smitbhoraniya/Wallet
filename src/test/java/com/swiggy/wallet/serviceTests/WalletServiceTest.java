package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.repositories.WalletRepository;
import com.swiggy.wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletServiceTest {
    @Autowired
    private WalletService walletService;
    @MockBean
    private WalletRepository walletRepository;
    @BeforeEach
    void setUp() {
        reset(walletRepository);
    }

    @Test
    void deposit_withValidAmount() {
        long walletId = 1;
        int depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseModel actualResponseModel = walletService.deposit(walletId, requestModel);

        WalletResponseModel responseModel = new WalletResponseModel(150);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(150, wallet.getMoney());
    }

    @Test
    void deposit_withNegativeAmount_shouldThrowInvalidMoneyException() {
        long walletId = 1;
        int depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidMoneyException.class, () -> walletService.deposit(walletId, requestModel));
        assertEquals(100, wallet.getMoney());
    }

    @Test
    void withdraw_withValidAmount() {
        long walletId = 1;
        int depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseModel actualResponseModel = walletService.withdraw(walletId, requestModel);

        WalletResponseModel responseModel = new WalletResponseModel(50);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(50, wallet.getMoney());
    }

    @Test
    void withdraw_withNegativeAmount_shouldThrowInvalidMoneyException() {
        long walletId = 1;
        int depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidMoneyException.class, () -> walletService.withdraw(walletId, requestModel));
        assertEquals(100, wallet.getMoney());
    }

    @Test
    void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() {
        long walletId = 1;
        int depositMoney = 150;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setMoney(100);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientMoneyException.class, () -> walletService.withdraw(walletId, requestModel));
        assertEquals(100, wallet.getMoney());
    }
}
