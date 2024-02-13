package com.swiggy.wallet.modelTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletTest {
    @Test
    public void deposit_withValidAmount() {
        Wallet wallet = new Wallet();
        wallet.deposit(100, Currency.RUPEE);

        assertEquals(100, wallet.getAmount());
    }

    @Test
    public void deposit_withNegativeAmount_shouldThrowInvalidMoneyException() {
        Wallet wallet = new Wallet();

        assertThrows(InvalidMoneyException.class, () -> wallet.deposit(-100, Currency.RUPEE));
    }

    @Test
    public void withdraw_withValidAmount() {
        Wallet wallet = new Wallet();
        wallet.setAmount(200.0);
        wallet.withdraw(100, Currency.RUPEE);

        assertEquals(100, wallet.getAmount());
    }

    @Test
    public void withdraw_withNegativeAmount_shouldThrowInvalidMoneyException() {
        Wallet wallet = new Wallet();

        assertThrows(InvalidMoneyException.class, () -> wallet.withdraw(-100, Currency.RUPEE));
    }

    @Test
    public void withdraw_withInsufficientFunds_shouldThrowInsufficientMoneyException() {
        Wallet wallet = new Wallet();

        assertThrows(InsufficientMoneyException.class, () -> wallet.withdraw(100, Currency.RUPEE));
    }
}
