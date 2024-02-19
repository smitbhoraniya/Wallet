package com.swiggy.wallet.services.interfaces;

import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.models.responseModels.UserResponseModel;

public interface IUserService {
    UserResponseModel register(UserRequestModel user);
    void delete();
}
