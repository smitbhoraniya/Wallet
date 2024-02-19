package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidAmountException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Money {
    private double amount = 0;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Money(double amount, Currency currency) {
        if (amount < 0) {
            throw new InvalidAmountException("Money should be positive.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public void subtract(Money money) {
        double amountInBaseCurrency = money.getCurrency().convertToBase(money.amount);
        if (this.amount < amountInBaseCurrency) {
            throw new InsufficientMoneyException("Don't have enough money.");
        }
        this.amount = this.amount - amountInBaseCurrency;
    }

    public void add(Money money) {
        double amountInBaseCurrency = money.getCurrency().convertToBase(money.amount);
        this.amount = this.amount + amountInBaseCurrency;
    }
}
