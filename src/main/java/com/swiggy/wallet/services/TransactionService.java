package com.swiggy.wallet.services;

import com.swiggy.wallet.enums.IntraWalletTransactionType;
import com.swiggy.wallet.execptions.*;
import com.swiggy.wallet.models.*;
import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.repositories.IntraWalletTransactionRepository;
import com.swiggy.wallet.repositories.TransactionRepository;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.repositories.WalletRepository;
import com.swiggy.wallet.services.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private IntraWalletTransactionRepository intraWalletTransactionRepository;
    @Override
    public TransactionResponseModel transaction(TransactionRequestModel transactionRequestModel) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUserName(username)
                .orElseThrow(() -> new AuthenticationFailedException("User " + username + " not found."));
        User receiver = userRepository.findByUserName(transactionRequestModel.getReceiverName())
                .orElseThrow(() -> new UserNotFoundException("User " + transactionRequestModel.getReceiverName() + " not found."));

        if (transactionRequestModel.getSenderWalletId() == transactionRequestModel.getReceiverWalletId()) {
            throw new SameUserTransactionException("Couldn't do transaction in same account.");
        }

        Wallet senderWallet = walletRepository.findByIdAndUser(transactionRequestModel.getSenderWalletId(), sender)
                .orElseThrow(() -> new WalletNotFoundException("Sender wallet not found."));
        Wallet receiverWallet = walletRepository.findByIdAndUser(transactionRequestModel.getReceiverWalletId(), receiver)
                .orElseThrow(() -> new WalletNotFoundException("Receiver wallet not found."));

        IntraWalletTransaction withdraw = intraWalletTransactionRepository.save(
                new IntraWalletTransaction(transactionRequestModel.getMoney(), IntraWalletTransactionType.WITHDRAW, senderWallet));
        IntraWalletTransaction deposit = intraWalletTransactionRepository.save(
                new IntraWalletTransaction(transactionRequestModel.getMoney(), IntraWalletTransactionType.DEPOSIT, receiverWallet));

        Transaction transaction = senderWallet.transfer(sender, receiver, receiverWallet, transactionRequestModel.getMoney());
        Transaction transactionToSave = new Transaction(sender, receiver, transaction.getServiceCharge(), deposit, withdraw);
        transactionRepository.save(transactionToSave);

        return new TransactionResponseModel(
                sender.getUserName(),
                receiver.getUserName(),
                deposit,
                withdraw,
                transactionToSave.getServiceCharge(),
                transactionToSave.getCreatedAt());
    }

    @Override
    public List<TransactionResponseModel> fetchTransactions(LocalDateTime... dateTimes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AuthenticationFailedException("User " + username + " not found."));

        List<Transaction> transactions;
        if (dateTimes.length > 0 && dateTimes[0] != null && dateTimes[1] != null) {
            transactions = transactionRepository.findBySenderOrRecipientNameAndDateTimeBefore(user, dateTimes[0], dateTimes[1]);
        } else {
            transactions = transactionRepository.findBySenderOrRecipientName(user);
        }

        List<IntraWalletTransaction> intraWalletTransactions;
        if (dateTimes.length > 0 && dateTimes[0] != null && dateTimes[1] != null) {
            intraWalletTransactions = intraWalletTransactionRepository.findAllByUserAndCreatedAt(user, dateTimes[0], dateTimes[1]);
        } else {
            intraWalletTransactions = intraWalletTransactionRepository.findAllByUserId(user);
        }
        for (Transaction transaction: transactions) {
            intraWalletTransactions.remove(transaction.getDeposit());
            intraWalletTransactions.remove(transaction.getWithdraw());
        }

        List<TransactionResponseModel> transactionResponseModelList = transactions.stream()
                .map(transaction ->
                        new TransactionResponseModel(
                                transaction.getSender().getUserName(),
                                transaction.getReceiver().getUserName(),
                                transaction.getDeposit(),
                                transaction.getWithdraw(),
                                transaction.getServiceCharge(),
                                transaction.getCreatedAt()
                        )
                )
                .toList();

        List<TransactionResponseModel> intraWalletTransactionResponse = intraWalletTransactions.stream()
                .map(transaction -> {
                    IntraWalletTransaction deposit = null;
                    IntraWalletTransaction withdraw = null;

                    if (transaction.getType() == IntraWalletTransactionType.DEPOSIT) {
                        deposit = transaction;
                    } else if (transaction.getType() == IntraWalletTransactionType.WITHDRAW) {
                        withdraw = transaction;
                    }

                    return new TransactionResponseModel(
                            null,
                            null,
                            deposit,
                            withdraw,
                            null,
                            transaction.getCreatedAt()
                    );}
                )
                .toList();

        List<TransactionResponseModel> finalResponse = new ArrayList<>();
        finalResponse.addAll(transactionResponseModelList);
        finalResponse.addAll(intraWalletTransactionResponse);
        return finalResponse;
    }
}
