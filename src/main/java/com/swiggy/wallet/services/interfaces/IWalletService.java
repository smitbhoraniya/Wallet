package com.swiggy.wallet.services.interfaces;

import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;

import java.util.List;

public interface IWalletService {
    WalletResponseModel withdraw(int id, String username, WalletRequestModel money);

    WalletResponseModel deposit(int id, String username, WalletRequestModel money);

    List<WalletResponseModel> fetchWallets();

    void transact(Wallet wallet, Wallet wallet1, Money money);
}
