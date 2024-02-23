package com.swiggy.wallet.models.requestModels;

import com.swiggy.wallet.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestModel {
    private String username;
    private String password;
    private Country country;
}
