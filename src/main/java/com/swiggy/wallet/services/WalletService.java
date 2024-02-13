package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService implements IWalletService {
    private final Wallet wallet;

    @Autowired
    public WalletService(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public WalletResponseModel withdraw(WalletRequestModel walletRequestModel) {
        if (walletRequestModel.getMoney() < 0) {
            throw new InvalidMoneyException("Money should be positive.");
        }
        if (wallet.getMoney() < walletRequestModel.getMoney()) {
            throw new InsufficientMoneyException("Don't have enough money.");
        }
        wallet.withdraw(walletRequestModel.getMoney());
        return new WalletResponseModel(wallet.getMoney());
    }

    @Override
    public WalletResponseModel deposit(WalletRequestModel walletRequestModel) {
        if (walletRequestModel.getMoney() < 0) {
            throw new InsufficientMoneyException("Money should be positive.") ;
        }
        wallet.deposit(walletRequestModel.getMoney());
        return new WalletResponseModel(wallet.getMoney());
    }
}
