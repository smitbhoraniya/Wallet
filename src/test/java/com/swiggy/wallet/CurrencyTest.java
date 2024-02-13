package com.swiggy.wallet;

import com.swiggy.wallet.enums.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyTest {
    @Test
    public void convertDollarToRupee() {
        Currency currency2 = Currency.DOLLAR;
        double amount = 10;

        double actual = currency2.convertToBase(amount);
        assertEquals(800, actual);
    }

    @Test
    public void convertRupeeToDollar() {
        Currency currency2 = Currency.RUPEE;
        double amount = 10;

        double actual = currency2.convertToBase(amount);
        assertEquals(10, actual);
    }
}
