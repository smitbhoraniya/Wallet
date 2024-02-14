package com.swiggy.wallet.modelTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class WalletTest {
    @MockBean
    private Money money;
    @Test
    public void deposit_withValidAmount() {
        Wallet wallet = new Wallet(1L, money);
        wallet.deposit(new Money(10, Currency.RUPEE));

        verify(money, times(1)).add(any(Money.class));
    }

    @Test
    public void withdraw_withValidAmount() {
        Wallet wallet = new Wallet(1L, money);
        wallet.withdraw(new Money(10, Currency.RUPEE));

        verify(money, times(1)).subtract(any(Money.class));
    }

    @Test
    public void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() {
        Wallet wallet = new Wallet();
        Money money = new Money(100, Currency.RUPEE);

        assertThrows(InsufficientMoneyException.class, () -> wallet.withdraw(money));
    }
}
