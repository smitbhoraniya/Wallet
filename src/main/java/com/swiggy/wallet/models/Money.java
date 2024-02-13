package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Money {
    private double amount;
    private Currency currency;
}
