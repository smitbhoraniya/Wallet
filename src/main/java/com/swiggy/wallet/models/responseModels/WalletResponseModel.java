package com.swiggy.wallet.models.responseModels;

import com.swiggy.wallet.models.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseModel {
    private Money money;
}
