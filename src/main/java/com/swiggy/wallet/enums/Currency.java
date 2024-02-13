package com.swiggy.wallet.enums;

import java.util.function.Function;

public enum Currency {
    RUPEE(amount -> amount),
    DOLLAR(amount -> amount * 80);
    private final Function<Double, Double> convertToBase;
    private Currency(Function<Double, Double> convertToBase) {
        this.convertToBase = convertToBase;
    }
    public Double convertToBase(Double amount) {
        return this.convertToBase.apply(amount);
    }
}
