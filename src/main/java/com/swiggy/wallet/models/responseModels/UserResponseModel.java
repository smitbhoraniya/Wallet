package com.swiggy.wallet.models.responseModels;

import com.swiggy.wallet.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {
    private String username;
    private Wallet wallet;
}
