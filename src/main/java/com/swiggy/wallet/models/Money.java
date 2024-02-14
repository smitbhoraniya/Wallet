package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Money {
    private double amount;
    private Currency currency;

    public Money(double amount, Currency currency) {
        if (amount < 0) {
            throw new InvalidMoneyException("Money should be positive.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public void subtract(Money money) {
        double amountInBaseCurrency = money.getCurrency().convertToBase(money.getAmount());
        if (this.amount < amountInBaseCurrency) {
            throw new InsufficientMoneyException("Don't have enough money.");
        }
        this.amount = this.amount - amountInBaseCurrency;
    }

    public void add(Money money) {
        double amountInBaseCurrency = money.getCurrency().convertToBase(money.getAmount());
        this.amount = this.amount + amountInBaseCurrency;
    }
}
