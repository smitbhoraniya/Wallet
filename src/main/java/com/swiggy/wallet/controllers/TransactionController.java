package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.services.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @PostMapping("")
    public ResponseEntity<TransactionResponseModel> transaction(@RequestBody TransactionRequestModel transactionRequestModel) {
        TransactionResponseModel response = transactionService.transaction(transactionRequestModel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<TransactionResponseModel>> fetchTransactions(
            @RequestParam(required = false) LocalDateTime fromDateTime,
            @RequestParam(required = false) LocalDateTime toDateTime) {
        List<TransactionResponseModel> response = transactionService.fetchTransactions(fromDateTime, toDateTime);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
