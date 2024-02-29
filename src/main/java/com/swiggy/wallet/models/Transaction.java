package com.swiggy.wallet.models;

import com.swiggy.wallet.enums.Currency;
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

    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "service_charge_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "service_charge_currency"))
    })
    private Money serviceCharge;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    private IntraWalletTransaction deposit;

    @OneToOne(cascade = CascadeType.ALL)
    private IntraWalletTransaction withdraw;

    public Transaction(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.serviceCharge = new Money(0, Currency.RUPEE);
    }

    public Transaction(User sender, User receiver, Money serviceCharge) {
        this.sender = sender;
        this.receiver = receiver;
        this.serviceCharge = serviceCharge;
    }

    public Transaction(User sender, User receiver, Money serviceCharge, IntraWalletTransaction deposit, IntraWalletTransaction withdraw) {
        this.sender = sender;
        this.receiver = receiver;
        this.serviceCharge = serviceCharge;
        this.deposit = deposit;
        this.withdraw = withdraw;
    }
}
