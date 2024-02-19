package com.swiggy.wallet.models.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestModel {
    private String username;
    private String password;
}
