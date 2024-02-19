package com.swiggy.wallet.services.interfaces;

import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITransactionService {
    TransactionResponseModel transaction(TransactionRequestModel transactionRequestModel);
    List<TransactionResponseModel> fetchTransactions(LocalDateTime... dateTimes);
}
