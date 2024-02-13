package com.swiggy.wallet.services;

import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;

public interface IWalletService {
    WalletResponseModel withdraw(WalletRequestModel money);
    WalletResponseModel deposit(WalletRequestModel money);
}
