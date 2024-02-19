package com.swiggy.wallet.modelTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class WalletTest {
    @Mock
    private Money money;
    @InjectMocks
    private Wallet wallet;
    @BeforeEach
    void setUp() {
        openMocks(this);
    }
    @Test
    public void expectDepositMoney() {
        wallet.deposit(new Money(10, Currency.RUPEE));

        verify(money, times(1)).add(any(Money.class));
    }

    @Test
    public void expectWithdrawMoney() {
        wallet.withdraw(new Money(10, Currency.RUPEE));

        verify(money, times(1)).subtract(any(Money.class));
    }

    @Test
    public void withdrawWithInsufficientFundsShouldThrowInsufficientMoneyException() {
        Wallet wallet = new Wallet();
        Money money = new Money(100, Currency.RUPEE);

        assertThrows(InsufficientMoneyException.class, () -> wallet.withdraw(money));
    }
}
