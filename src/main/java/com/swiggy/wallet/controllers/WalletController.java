package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping(value = "/{id}/intra-wallet-transaction", headers = "type=withdraw")
    public ResponseEntity<WalletResponseModel> withdraw(@RequestBody WalletRequestModel money, @PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(walletService.withdraw(id, username, money));
    }

    @PostMapping(value = "/{id}/intra-wallet-transaction", headers = "type=deposit")
    public ResponseEntity<WalletResponseModel> deposit(@RequestBody WalletRequestModel money, @PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(walletService.deposit(id, username, money));
    }

    @GetMapping("")
    public ResponseEntity<List<WalletResponseModel>> fetchWallets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(walletService.fetchWallets(username));
    }

    @PostMapping("")
    public ResponseEntity<WalletResponseModel> create() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new ResponseEntity<>(walletService.create(username), HttpStatus.CREATED);
    }
}
