package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.WalletRequestModel;
import com.swiggy.wallet.models.WalletResponseModel;
import com.swiggy.wallet.services.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private IWalletService walletService;

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<WalletResponseModel> withdraw(@PathVariable Long id, @RequestBody WalletRequestModel money) {
        return ResponseEntity.ok(walletService.withdraw(id, money));
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<WalletResponseModel> deposit(@PathVariable Long id, @RequestBody WalletRequestModel money) {
        return ResponseEntity.ok(walletService.deposit(id, money));
    }

    @PostMapping("")
    public ResponseEntity<WalletResponseModel> create() {
        return ResponseEntity.ok(walletService.create());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseModel> getWalletById(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getWalletById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<WalletResponseModel>> getAllWallet() {
        return ResponseEntity.ok(walletService.getAllWallets());
    }
}
