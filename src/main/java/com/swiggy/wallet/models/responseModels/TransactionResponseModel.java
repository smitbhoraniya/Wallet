package com.swiggy.wallet.models.responseModels;

import com.swiggy.wallet.models.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseModel {
    private String senderUsername;
    private String receiverUsername;
    private Money transferredAmount;
}
