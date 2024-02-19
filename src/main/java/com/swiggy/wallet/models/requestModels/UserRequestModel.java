package com.swiggy.wallet.models.requestModels;

import lombok.*;

@Data
@AllArgsConstructor
public class UserRequestModel {
    private String username;
    private String password;
}
