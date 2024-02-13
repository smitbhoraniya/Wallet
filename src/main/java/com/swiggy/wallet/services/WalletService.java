package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidMoneyException;
import com.swiggy.wallet.execptions.WalletNotFoundException;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService implements IWalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public WalletResponseModel withdraw(Long id, WalletRequestModel walletRequestModel) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        wallet.withdraw(walletRequestModel.getAmount(), walletRequestModel.getCurrency());
        walletRepository.save(wallet);
        return new WalletResponseModel(wallet.getAmount());
    }

    @Override
    public WalletResponseModel deposit(Long id, WalletRequestModel walletRequestModel) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        wallet.deposit(walletRequestModel.getAmount(), walletRequestModel.getCurrency());
        walletRepository.save(wallet);
        return new WalletResponseModel(wallet.getAmount());
    }

    @Override
    public WalletResponseModel create() {
        Wallet wallet = walletRepository.save(new Wallet());
        return new WalletResponseModel(wallet.getAmount());
    }

    @Override
    public WalletResponseModel checkBalance(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        return new WalletResponseModel(wallet.getAmount());
    }
}
