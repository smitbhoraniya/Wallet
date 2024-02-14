package com.swiggy.wallet.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseModel {
    private Long id;
    private Money money;
}
