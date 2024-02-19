package com.swiggy.wallet.models.requestModels;

import com.swiggy.wallet.enums.Currency;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestModel {
    private Double amount;
    @Enumerated
    private Currency currency;
}
