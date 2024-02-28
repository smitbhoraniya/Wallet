package com.swiggy.wallet.enums;

import lombok.Getter;

import static com.swiggy.wallet.enums.Currency.DOLLAR;
import static com.swiggy.wallet.enums.Currency.RUPEE;

@Getter
public enum Country {
    INDIA(RUPEE),
    AMERICA(DOLLAR);
    private final Currency currency;

    Country(Currency currency) {
        this.currency = currency;
    }

}
