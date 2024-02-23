package com.swiggy.wallet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Money money;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Wallet(User user) {
        this.money = new Money(0.0, user.getCountry().getCurrency());
        this.user = user;
    }

    public void withdraw(Money money) {
        this.money.subtract(money);
    }

    public void deposit(Money money) {
        this.money.add(money);
    }
}
