package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.AuthenticationFailedException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.repositories.WalletRepository;
import com.swiggy.wallet.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService implements IWalletService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public WalletResponseModel withdraw(String username, WalletRequestModel walletRequestModel) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));

        user.getWallet().withdraw(new Money(walletRequestModel.getAmount(), walletRequestModel.getCurrency()));
        userRepository.save(user);
        return new WalletResponseModel(user.getWallet().getMoney());
    }

    @Override
    public WalletResponseModel deposit(String username, WalletRequestModel walletRequestModel) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));

        user.getWallet().deposit(new Money(walletRequestModel.getAmount(), walletRequestModel.getCurrency()));
        userRepository.save(user);
        return new WalletResponseModel(user.getWallet().getMoney());
    }

    @Override
    public List<WalletResponseModel> fetchWallets() {
        return walletRepository.findAll().stream().map(wallet -> new WalletResponseModel(wallet.getMoney())).toList();
    }

    @Override
    public void transact(Wallet senderWallet, Wallet receiverWallet, Money money) {
        senderWallet.withdraw(money);
        receiverWallet.deposit(money);
    }
}
