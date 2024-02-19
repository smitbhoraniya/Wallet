package com.swiggy.wallet.models;

import jakarta.persistence.*;
import lombok.*;

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

    public Transaction(User sender, User receiver, Money transferredMoney) {
        this.sender = sender;
        this.receiver = receiver;
        this.transferredMoney = transferredMoney;
    }
}
