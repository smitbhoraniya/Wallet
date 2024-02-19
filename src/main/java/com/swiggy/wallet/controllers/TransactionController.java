package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.services.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @PutMapping("")
    public ResponseEntity<TransactionResponseModel> transaction(@RequestBody TransactionRequestModel transactionRequestModel) {
        TransactionResponseModel response = transactionService.transaction(transactionRequestModel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<TransactionResponseModel>> fetchTransactions() {
        List<TransactionResponseModel> response = transactionService.fetchTransactions();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
