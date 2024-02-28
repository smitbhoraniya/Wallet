package com.swiggy.wallet.services;

import com.swiggy.wallet.enums.IntraWalletTransactionType;
import com.swiggy.wallet.execptions.AuthenticationFailedException;
import com.swiggy.wallet.execptions.WalletNotFoundException;
import com.swiggy.wallet.models.IntraWalletTransaction;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.repositories.IntraWalletTransactionRepository;
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
    @Autowired
    private IntraWalletTransactionRepository intraWalletTransactionRepository;

    @Override
    public WalletResponseModel withdraw(int id, String username, WalletRequestModel walletRequestModel) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));
        Wallet wallet = walletRepository.findByIdAndUser(id, user).orElseThrow(() -> new WalletNotFoundException("Wallet not found."));

        Money money = new Money(walletRequestModel.getAmount(), walletRequestModel.getCurrency());
        wallet.withdraw(money);
        userRepository.save(user);
        intraWalletTransactionRepository.save(new IntraWalletTransaction(money, IntraWalletTransactionType.WITHDRAW, wallet));
        return new WalletResponseModel(wallet.getId(), wallet.getMoney());
    }

    @Override
    public WalletResponseModel deposit(int id, String username, WalletRequestModel walletRequestModel) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));
        Wallet wallet = walletRepository.findByIdAndUser(id, user).orElseThrow(() -> new WalletNotFoundException("Wallet not found."));

        Money money = new Money(walletRequestModel.getAmount(), walletRequestModel.getCurrency());
        wallet.deposit(money);
        userRepository.save(user);
        intraWalletTransactionRepository.save(new IntraWalletTransaction(money, IntraWalletTransactionType.DEPOSIT, wallet));
        return new WalletResponseModel(wallet.getId(), wallet.getMoney());
    }

    @Override
    public List<WalletResponseModel> fetchWallets(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));
        return walletRepository.findAllByUser(user).stream().map(wallet -> new WalletResponseModel(wallet.getId(), wallet.getMoney())).toList();
    }

    @Override
    public WalletResponseModel create(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));
        Wallet wallet = new Wallet(user);

        Wallet response = walletRepository.save(wallet);
        return new WalletResponseModel(response.getId(), response.getMoney());
    }
}
