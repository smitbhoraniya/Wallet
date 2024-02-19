package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.SameUserTransactionException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        if (sender.getUserId() == receiver.getUserId()) {
            throw new SameUserTransactionException("Couldn't do transaction in same account.");
        }

        walletService.transact(sender.getWallet(), receiver.getWallet(), transactionRequestModel.getMoney());

        userRepository.save(sender);
        userRepository.save(receiver);
        Transaction transactionToSave = new Transaction(sender, receiver, transactionRequestModel.getMoney());
        transactionRepository.save(transactionToSave);

        return new TransactionResponseModel(sender.getUserName(), receiver.getUserName(), transactionRequestModel.getMoney(), transactionToSave.getCreatedAt());
    }

    @Override
    public List<TransactionResponseModel> fetchTransactions(LocalDateTime... dateTimes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User "+ username + " not found."));

        List<Transaction> transactions = new ArrayList<>();
        if (dateTimes.length > 0 && dateTimes[0] != null && dateTimes[1] != null) {
            transactions = transactionRepository.findBySenderOrRecipientNameAndDateTimeBefore(user, dateTimes[0], dateTimes[1]);
        }
        else {
        transactions = transactionRepository.findBySenderOrRecipientName(user);
        }

        return transactions.stream()
                .map(transaction ->
                        new TransactionResponseModel(
                                transaction.getSender().getUserName(),
                                transaction.getReceiver().getUserName(),
                                transaction.getTransferredMoney(),
                                transaction.getCreatedAt()
                        )
                )
                .toList();
    }
}
