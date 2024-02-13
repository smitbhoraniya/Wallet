package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class WalletService implements IWalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public WalletResponseModel withdraw(Long id, WalletRequestModel walletRequestModel) {
        if (walletRequestModel.getMoney() < 0) {
            throw new InvalidMoneyException("Money should be positive.");
        }

        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Wallet not found"));
        if (wallet.getMoney() < walletRequestModel.getMoney()) {
            throw new InsufficientMoneyException("Don't have enough money.");
        }

        int updatedBalance = wallet.getMoney() - walletRequestModel.getMoney();
        wallet.setMoney(updatedBalance);
        walletRepository.save(wallet);
        return new WalletResponseModel(wallet.getMoney());
    }

    @Override
    public WalletResponseModel deposit(Long id, WalletRequestModel walletRequestModel) {
        if (walletRequestModel.getMoney() < 0) {
            throw new InsufficientMoneyException("Money should be positive.") ;
        }
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Wallet not found"));

        int updatedBalance = wallet.getMoney() + walletRequestModel.getMoney();
        wallet.setMoney(updatedBalance);
        walletRepository.save(wallet);
        return new WalletResponseModel(wallet.getMoney());
    }

    @Override
    public WalletResponseModel create() {
        Wallet wallet = walletRepository.save(new Wallet());
        return new WalletResponseModel(wallet.getMoney());
    }

    @Override
    public WalletResponseModel checkBalance(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Wallet not found"));
        return new WalletResponseModel(wallet.getMoney());
    }
}
