package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Money money;

    public Wallet() {
        this.money = new Money(0.0, Currency.RUPEE);
    }

    public void withdraw(Money money) {
        this.money.subtract(money);
    }

    public void deposit(Money money) {
        this.money.add(money);
    }
}
