package com.swiggy.wallet.controllers;

import com.swiggy.wallet.clients.GreetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class GRPCTest {
    @Autowired
    private GreetClient greetClient;

    @GetMapping("")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok(greetClient.greet("Smit"));
    }
}
