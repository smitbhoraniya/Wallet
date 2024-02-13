package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    public Wallet() {
        this.amount = 0.0;
    }
    public void withdraw(double amount, Currency currency) {
        if (amount < 0) {
            throw new InvalidMoneyException("Money should be positive.");
        }

        double amountInBaseCurrency = currency.convertToBase(amount);
        if (this.amount < amountInBaseCurrency) {
            throw new InsufficientMoneyException("Don't have enough money.");
        }
        this.amount = this.amount - amountInBaseCurrency;
    }

    public void deposit(double amount, Currency currency) {
        if (amount < 0) {
            throw new InvalidMoneyException("Money should be positive.");
        }
        double amountInBaseCurrency = currency.convertToBase(amount);
        this.amount = this.amount + amountInBaseCurrency;
    }
}
