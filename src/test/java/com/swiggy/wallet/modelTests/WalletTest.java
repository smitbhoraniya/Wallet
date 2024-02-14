package com.swiggy.wallet.modelTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletTest {
    @Test
    public void deposit_withValidAmount() {
        Wallet wallet = new Wallet();
        Money money = new Money(100, Currency.RUPEE);
        wallet.deposit(money);

        assertEquals(100, wallet.getMoney().getAmount());
    }

    @Test
    public void withdraw_withValidAmount() {
        Wallet wallet = new Wallet();
        Money money = new Money(100, Currency.RUPEE);
        wallet.setMoney(money);

        wallet.withdraw(money);

        assertEquals(0, wallet.getMoney().getAmount());
    }

    @Test
    public void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() {
        Wallet wallet = new Wallet();
        Money money = new Money(100, Currency.RUPEE);

        assertThrows(InsufficientMoneyException.class, () -> wallet.withdraw(money));
    }
}
