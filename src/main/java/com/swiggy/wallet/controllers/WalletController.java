package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    @Autowired
    private IWalletService walletService;

    @PutMapping("/withdraw")
    public ResponseEntity<WalletResponseModel> withdraw(@RequestBody WalletRequestModel money) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(walletService.withdraw(username, money));
    }

    @PutMapping("/deposit")
    public ResponseEntity<WalletResponseModel> deposit(@RequestBody WalletRequestModel money) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(walletService.deposit(username, money));
    }

    @GetMapping("")
    public ResponseEntity<List<WalletResponseModel>> fetchWallets() {
        return ResponseEntity.ok(walletService.fetchWallets());
    }
}
