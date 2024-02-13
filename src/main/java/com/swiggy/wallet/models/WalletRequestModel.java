package com.swiggy.wallet.models;

public class WalletRequestModel {
    private int money;

    public WalletRequestModel() {}

    public WalletRequestModel(int money) {
        this.money = money;
    }
    public int getMoney() {
        return this.money;
    }
}
