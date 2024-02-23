package com.swiggy.wallet.models.responseModels;

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
    private Money transferredAmount;
    private LocalDateTime createdAt;
    private Money serviceCharge;
}
