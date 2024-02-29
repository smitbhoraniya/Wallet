package com.swiggy.wallet.modelTests;

import com.swiggy.wallet.clients.CurrencyConvertClient;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidAmountException;
import com.swiggy.wallet.models.Money;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MoneyTest {
    @Test
    public void addValidMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.RUPEE);
        money.add(money1);

        assertEquals(new Money(200, Currency.RUPEE), money);
    }

    @Test
    public void addTwoValidDifferentMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.DOLLAR);
        money.add(money1);

        assertEquals(new Money(8100, Currency.RUPEE), money);
    }

    @Test
    public void moneyWithNegativeAmount() {
        assertThrows(InvalidAmountException.class, () -> new Money(-100, Currency.RUPEE));
    }

    @Test
    public void subtractValidMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.RUPEE);
        money.subtract(money1);

        assertEquals(new Money(0, Currency.RUPEE), money);
    }

    @Test
    public void subtractTwoValidDifferentMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(1, Currency.DOLLAR);
        money.subtract(money1);

        assertEquals(new Money(20, Currency.RUPEE), money);
    }

    @Test
    public void subtractMoneyFromLesserMoney() {
        Money money = new Money(100, Currency.RUPEE);
        Money money1 = new Money(100, Currency.DOLLAR);

        assertThrows(InsufficientMoneyException.class, () -> money.subtract(money1));
    }
}
