package com.swiggy.wallet.models.requestModels;

import com.swiggy.wallet.enums.Currency;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestModel {
    private Double amount;
    @Enumerated
    private Currency currency;
}
