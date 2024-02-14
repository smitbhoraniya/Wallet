package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.execptions.NotFoundException;
import com.swiggy.wallet.models.Money;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletServiceTest {
    @Autowired
    private WalletService walletService;
    @MockBean
    private WalletRepository walletRepository;
    @MockBean
    private Wallet wallet;
    @BeforeEach
    void setUp() {
        reset(walletRepository);
    }

    @Test
    void deposit_withValidAmount() {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.deposit(walletId, requestModel);

        verify(wallet, times(1)).deposit(any(Money.class));
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void deposit_withNegativeAmount_shouldThrowInvalidMoneyException() {
        long walletId = 1;
        double depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidMoneyException.class, () -> walletService.deposit(walletId, requestModel));
    }

    @Test
    void withdraw_withValidAmount() {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.withdraw(walletId, requestModel);

        verify(wallet, times(1)).withdraw(any(Money.class));
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void withdraw_withNegativeAmount_shouldThrowInvalidMoneyException() {
        long walletId = 1;
        double depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidMoneyException.class, () -> walletService.withdraw(walletId, requestModel));
    }

    @Test
    void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() {
        long walletId = 1;
        double depositMoney = 150;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        Wallet wallet1 = new Wallet();
        wallet1.deposit(new Money(100, Currency.RUPEE));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet1));

        assertThrows(InsufficientMoneyException.class, () -> walletService.withdraw(walletId, requestModel));
    }

    @Test
    void deposit_withValidDollar() {
        long walletId = 1;
        double depositMoney = 1;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.DOLLAR);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.deposit(walletId, requestModel);

        verify(wallet, times(1)).deposit(any(Money.class));
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void withdraw_withValidDollar() {
        long walletId = 1;
        double depositMoney = 1;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.DOLLAR);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.withdraw(walletId, requestModel);

        verify(wallet, times(1)).withdraw(any(Money.class));
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void create_validWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setMoney(new Money(0, Currency.RUPEE));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        WalletResponseModel response = walletService.create();

        verify(walletRepository, times(1)).save(any(Wallet.class));
        assertNotNull(response);
        assertEquals(0, response.getMoney().getAmount());
    }

    @Test
    void get_validWallet() {
        long walletId = 1;
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.checkBalance(walletId);

        verify(walletRepository, times(1)).findById(anyLong());
    }

    @Test
    void get_inValidWallet() {
        assertThrows(NotFoundException.class, () -> walletService.checkBalance(1L));
    }
}
