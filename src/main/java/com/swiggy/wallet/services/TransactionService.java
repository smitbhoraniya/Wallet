package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.Transaction;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.repositories.TransactionRepository;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.services.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletService walletService;
    @Override
    public TransactionResponseModel transaction(TransactionRequestModel transactionRequestModel) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found."));
        User receiver = userRepository.findByUserName(transactionRequestModel.getReceiverName())
                .orElseThrow(() -> new UserNotFoundException("User "+ transactionRequestModel.getReceiverName() + " not found."));

        walletService.transact(sender.getWallet(), receiver.getWallet(), transactionRequestModel.getMoney());

        userRepository.save(sender);
        userRepository.save(receiver);
        transactionRepository.save(new Transaction(sender, receiver, transactionRequestModel.getMoney()));

        return new TransactionResponseModel(sender.getUserName(), receiver.getUserName(), transactionRequestModel.getMoney());
    }

    @Override
    public List<TransactionResponseModel> fetchTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User "+ username + " not found."));

        List<Transaction> transactions = transactionRepository.findBySenderOrRecipientName(user);

        return transactions.stream()
                .map(transaction ->
                        new TransactionResponseModel(
                                transaction.getSender().getUserName(),
                                transaction.getReceiver().getUserName(),
                                transaction.getTransferredMoney()
                        )
                )
                .toList();
    }
}
