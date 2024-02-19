package com.swiggy.wallet.models.responseModels;

import com.swiggy.wallet.models.Money;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseModel {
    private Money money;
}
