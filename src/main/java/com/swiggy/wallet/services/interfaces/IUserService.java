package com.swiggy.wallet.services.interfaces;

import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.responseModels.UserResponseModel;

public interface IUserService {
    UserResponseModel register(UserRequestModel user);

    void delete();
}
