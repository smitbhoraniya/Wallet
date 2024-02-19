package com.swiggy.wallet.services.interfaces;

import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;

import java.util.List;

public interface IWalletService {
    WalletResponseModel withdraw(String username, WalletRequestModel money);
    WalletResponseModel deposit(String username, WalletRequestModel money);
    List<WalletResponseModel> fetchWallets();
}
