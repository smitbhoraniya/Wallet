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
    @BeforeEach
    void setUp() {
        reset(walletRepository);
    }

    @Test
    void deposit_withValidAmount() {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseModel actualResponseModel = walletService.deposit(walletId, requestModel);

        Money responseMoney = new Money(150.0, Currency.RUPEE);
        WalletResponseModel responseModel = new WalletResponseModel(responseMoney);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(responseMoney, wallet.getMoney());
    }

    @Test
    void deposit_withNegativeAmount_shouldThrowInvalidMoneyException() {
        long walletId = 1;
        double depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidMoneyException.class, () -> walletService.deposit(walletId, requestModel));
        assertEquals(money, wallet.getMoney());
    }

    @Test
    void withdraw_withValidAmount() {
        long walletId = 1;
        double depositMoney = 50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseModel actualResponseModel = walletService.withdraw(walletId, requestModel);

        Money responseMoney = new Money(50, Currency.RUPEE);
        WalletResponseModel responseModel = new WalletResponseModel(responseMoney);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(responseMoney, wallet.getMoney());
    }

    @Test
    void withdraw_withNegativeAmount_shouldThrowInvalidMoneyException() {
        long walletId = 1;
        double depositMoney = -50;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidMoneyException.class, () -> walletService.withdraw(walletId, requestModel));
        assertEquals(money, wallet.getMoney());
    }

    @Test
    void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() {
        long walletId = 1;
        double depositMoney = 150;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.RUPEE);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientMoneyException.class, () -> walletService.withdraw(walletId, requestModel));
        assertEquals(money, wallet.getMoney());
    }

    @Test
    void deposit_withValidDollar() {
        long walletId = 1;
        double depositMoney = 1;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.DOLLAR);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseModel actualResponseModel = walletService.deposit(walletId, requestModel);

        Money responseMoney = new Money(180.0, Currency.RUPEE);
        WalletResponseModel responseModel = new WalletResponseModel(responseMoney);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(responseMoney, wallet.getMoney());
    }

    @Test
    void withdraw_withValidDollar() {
        long walletId = 1;
        double depositMoney = 1;
        WalletRequestModel requestModel = new WalletRequestModel(depositMoney, Currency.DOLLAR);
        Wallet wallet = new Wallet();
        Money money = new Money(100.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseModel actualResponseModel = walletService.withdraw(walletId, requestModel);

        Money responseMoney = new Money(20.0, Currency.RUPEE);
        WalletResponseModel responseModel = new WalletResponseModel(responseMoney);
        assertEquals(responseModel, actualResponseModel);
        assertEquals(responseMoney, wallet.getMoney());
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
        Wallet wallet = new Wallet();
        Money money = new Money(0.0, Currency.RUPEE);
        wallet.setId(walletId);
        wallet.setMoney(money);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        WalletResponseModel actualResponseModel = walletService.checkBalance(walletId);

        WalletResponseModel expected = new WalletResponseModel(new Money(0, Currency.RUPEE));
        assertEquals(expected, actualResponseModel);
    }

    @Test
    void get_inValidWallet() {
        assertThrows(NotFoundException.class, () -> walletService.checkBalance(1L));
    }
}
