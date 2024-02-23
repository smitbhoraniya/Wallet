package com.swiggy.wallet.enums;

import static com.swiggy.wallet.enums.Currency.DOLLAR;
import static com.swiggy.wallet.enums.Currency.RUPEE;

public enum Country {
    INDIA(RUPEE),
    AMERICA(DOLLAR);
    private final Currency currency;

    Country(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return this.currency;
    }
}
