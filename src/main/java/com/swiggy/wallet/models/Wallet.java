package com.swiggy.wallet.models;

import com.swiggy.wallet.clients.CurrencyConvertClient;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.enums.IntraWalletTransactionType;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Money money;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Wallet(User user) {
        this.money = new Money(0.0, user.getCountry().getCurrency());
        this.user = user;
    }

    public void withdraw(Money money) {
        this.money.subtract(money);
    }

    public void deposit(Money money) {
        this.money.add(money);
    }

    public Transaction transfer(User sender, User receiver, Wallet receiverWallet, Money money) {
        double serviceCharge = 0;
        double amountInWalletCurrency = money.getAmount();
        if (this.getMoney().getCurrency() != receiverWallet.getMoney().getCurrency()) {
            double amountInBaseCurrency = CurrencyConvertClient.convert(money, Currency.RUPEE);
            if (amountInBaseCurrency - 10 < 0) {
                throw new InsufficientMoneyException("Transfer money is less than service charge.");
            }
            double amountToTransfer = amountInBaseCurrency - 10;
            serviceCharge = 10;
            amountInWalletCurrency = CurrencyConvertClient.convert(new Money(amountToTransfer, Currency.RUPEE), money.getCurrency());
        }

        this.withdraw(money);
        receiverWallet.deposit(new Money(amountInWalletCurrency, money.getCurrency()));

        return new Transaction(sender, receiver, new Money(serviceCharge, Currency.RUPEE));
    }
}
