package com.swiggy.wallet.services;

import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;

import java.util.List;

public interface IWalletService {
    WalletResponseModel withdraw(Long id, WalletRequestModel money);
    WalletResponseModel deposit(Long id, WalletRequestModel money);
    WalletResponseModel create();
    WalletResponseModel getWalletById(Long id);
    List<WalletResponseModel> getAllWallets();
}
