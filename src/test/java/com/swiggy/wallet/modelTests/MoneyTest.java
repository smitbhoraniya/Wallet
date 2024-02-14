package com.swiggy.wallet.modelTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoneyTest {
    @Test
    public void addValidMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.RUPEE);
        money.add(money1);

        assertEquals(200, money.getAmount());
    }

    @Test
    public void addTwoValidDifferentMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.DOLLAR);
        money.add(money1);

        assertEquals(8100, money.getAmount());
    }

    @Test
    public void moneyWithNegativeAmount() {
        assertThrows(InvalidMoneyException.class, () -> new Money(-100, Currency.RUPEE));
    }

    @Test
    public void subtractValidMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.RUPEE);
        money.subtract(money1);

        assertEquals(0, money.getAmount());
    }

    @Test
    public void subtractTwoValidDifferentMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(1, Currency.DOLLAR);
        money.subtract(money1);

        assertEquals(20, money.getAmount());
    }

    @Test
    public void subtractMoneyFromLesserMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.DOLLAR);

        assertThrows(InsufficientMoneyException.class, () -> money.subtract(money1));
    }
}
