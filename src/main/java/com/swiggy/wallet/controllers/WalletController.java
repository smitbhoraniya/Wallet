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

    @GetMapping("")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponseModel> withdraw(@RequestBody WalletRequestModel money) {
        return new ResponseEntity<>(walletService.withdraw(money), HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponseModel> deposit(@RequestBody WalletRequestModel money) {
        return new ResponseEntity<>(walletService.deposit(money), HttpStatus.OK);
    }
}
