package com.swiggy.wallet.enums;

import java.util.function.Function;

public enum Currency {
    RUPEE(amount -> amount, amount -> amount),
    DOLLAR(amount -> amount * 80, amount -> amount / 80);
    private final Function<Double, Double> convertToBase;
    private final Function<Double, Double> convertFromBase;
    private Currency(Function<Double, Double> convertToBase, Function<Double, Double> convertFromBase) {
        this.convertToBase = convertToBase;
        this.convertFromBase = convertFromBase;
    }
    public Double convertToBase(Double amount) {
        return this.convertToBase.apply(amount);
    }

    public Double convertFromBase(Double amount) {
        return this.convertFromBase.apply(amount);
    }
}
