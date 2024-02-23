package com.swiggy.wallet.models.requestModels;

import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestModel {
    private String receiverName;
    private int senderWalletId;
    private int receiverWalletId;
    private Money money;
}
