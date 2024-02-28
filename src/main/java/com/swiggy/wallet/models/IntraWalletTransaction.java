package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.IntraWalletTransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntraWalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Money money;

    @Enumerated(EnumType.STRING)
    private IntraWalletTransactionType type;

    @ManyToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    public IntraWalletTransaction(Money money, IntraWalletTransactionType type, Wallet wallet) {
        this.money = money;
        this.type = type;
        this.wallet = wallet;
    }
}
