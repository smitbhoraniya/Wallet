package com.swiggy.wallet.models;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Wallet {
    private int money;
    public Wallet() {
        this.money = 0;
    }

    public int getMoney() {
        return this.money;
    }

    public void deposit(int money) {
        this.money += money;
    }

    public void withdraw(int money) {
        this.money -= money;
    }
}
