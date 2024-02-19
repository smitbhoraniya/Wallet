package com.swiggy.wallet.models.responseModels;

import com.swiggy.wallet.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {
    private String username;
    private Wallet wallet;
}
