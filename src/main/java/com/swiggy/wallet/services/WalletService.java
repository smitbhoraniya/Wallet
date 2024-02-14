package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.NotFoundException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService implements IWalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public WalletResponseModel withdraw(Long id, WalletRequestModel walletRequestModel) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new NotFoundException("Wallet not found"));

        wallet.withdraw(new Money(walletRequestModel.getAmount(), walletRequestModel.getCurrency()));
        walletRepository.save(wallet);
        return new WalletResponseModel(wallet.getId(), wallet.getMoney());
    }

    @Override
    public WalletResponseModel deposit(Long id, WalletRequestModel walletRequestModel) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new NotFoundException("Wallet not found"));

        wallet.deposit(new Money(walletRequestModel.getAmount(), walletRequestModel.getCurrency()));
        walletRepository.save(wallet);
        return new WalletResponseModel(wallet.getId(), wallet.getMoney());
    }

    @Override
    public WalletResponseModel create() {
        Wallet wallet = walletRepository.save(new Wallet());
        return new WalletResponseModel(wallet.getId(), wallet.getMoney());
    }

    @Override
    public WalletResponseModel getWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new NotFoundException("Wallet not found"));
        return new WalletResponseModel(wallet.getId(), wallet.getMoney());
    }

    @Override
    public List<WalletResponseModel> getAllWallets() {
        List<WalletResponseModel> wallets = walletRepository.findAll().stream().map(w -> new WalletResponseModel(w.getId(), w.getMoney())).toList();
        return wallets;
    }
}
