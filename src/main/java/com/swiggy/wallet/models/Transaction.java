package com.swiggy.wallet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL)
    private User receiver;

    private Money transferredMoney;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Transaction(User sender, User receiver, Money transferredMoney) {
        this.sender = sender;
        this.receiver = receiver;
        this.transferredMoney = transferredMoney;
    }
}
