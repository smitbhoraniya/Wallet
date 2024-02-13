package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.services.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private IWalletService walletService;

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<WalletResponseModel> withdraw(@PathVariable Long id, @RequestBody WalletRequestModel money) {
        return new ResponseEntity<>(walletService.withdraw(id, money), HttpStatus.OK);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<WalletResponseModel> deposit(@PathVariable Long id, @RequestBody WalletRequestModel money) {
        return new ResponseEntity<>(walletService.deposit(id, money), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<WalletResponseModel> create() {
        return new ResponseEntity<>(walletService.create(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseModel> checkBalance(@PathVariable Long id) {
        return new ResponseEntity<>(walletService.checkBalance(id), HttpStatus.OK);
    }
}
