package com.swiggy.wallet.models.responseModels;

import com.swiggy.wallet.models.IntraWalletTransaction;
import com.swiggy.wallet.models.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseModel {
    private String senderUsername;
    private String receiverUsername;
    private IntraWalletTransaction deposit;
    private IntraWalletTransaction withdraw;
    private Money serviceCharge;
    private LocalDateTime createdAt;
}
