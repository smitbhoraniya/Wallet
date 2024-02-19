package com.swiggy.wallet.services.interfaces;

import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.UserRequestModel;

public interface IUserService {
    User register(UserRequestModel user);
}
