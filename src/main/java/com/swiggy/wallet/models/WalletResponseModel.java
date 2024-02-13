package com.swiggy.wallet.models;

public class WalletResponseModel {
    private int money;

    public WalletResponseModel() {}

    public WalletResponseModel(int money) {
        this.money = money;
    }
    public int getMoney() {
        return this.money;
    }
}
