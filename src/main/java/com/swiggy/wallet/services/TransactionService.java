package com.swiggy.wallet.services;

import com.swiggy.wallet.enums.Currency;
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

        senderWallet.withdraw(transactionRequestModel.getMoney());
        receiverWallet.deposit(transactionRequestModel.getMoney());

        IntraWalletTransaction withdraw = intraWalletTransactionRepository.save(
                new IntraWalletTransaction(transactionRequestModel.getMoney(), IntraWalletTransactionType.WITHDRAW, senderWallet));
        IntraWalletTransaction deposit = intraWalletTransactionRepository.save(
                new IntraWalletTransaction(transactionRequestModel.getMoney(), IntraWalletTransactionType.DEPOSIT, receiverWallet));

        double serviceCharge = 0;
        if (senderWallet.getMoney().getCurrency() != receiverWallet.getMoney().getCurrency()) {
            double amountInBaseCurrency = transactionRequestModel.getMoney().getCurrency().convertToBase(transactionRequestModel.getMoney().getAmount());
            if (amountInBaseCurrency - 10 < 0) {
                throw new InsufficientMoneyException("Transfer money is less than service charge.");
            }
            double amountToTransfer = amountInBaseCurrency - 10;
            serviceCharge = 10;
            double amountInWalletCurrency = transactionRequestModel.getMoney().getCurrency().convertFromBase(amountToTransfer);
            transactionRequestModel.getMoney().setAmount(amountInWalletCurrency);
        }

        Transaction transactionToSave = new Transaction(sender, receiver, transactionRequestModel.getMoney(), new Money(serviceCharge, Currency.RUPEE), deposit, withdraw);
        transactionRepository.save(transactionToSave);

        return new TransactionResponseModel(sender.getUserName(), receiver.getUserName(), transactionRequestModel.getMoney(), transactionToSave.getCreatedAt(), new Money(serviceCharge, Currency.RUPEE));
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

        return transactions.stream()
                .map(transaction ->
                        new TransactionResponseModel(
                                transaction.getSender().getUserName(),
                                transaction.getReceiver().getUserName(),
                                transaction.getTransferredMoney(),
                                transaction.getCreatedAt(),
                                transaction.getServiceCharge()
                        )
                )
                .toList();
    }
}
