package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import jakarta.persistence.*;
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
    @Embedded
    private Money money;
    public Wallet() {
        this.money = new Money(0, Currency.RUPEE);
    }
    public void withdraw(Money money) {
        this.money.subtract(money);
    }

    public void deposit(Money money) {
        this.money.add(money);
    }
}
