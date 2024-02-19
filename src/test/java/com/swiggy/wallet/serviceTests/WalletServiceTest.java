package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.AuthenticationFailedException;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidAmountException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.repositories.WalletRepository;
import com.swiggy.wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Wallet wallet;
    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void expectDepositMoney() {
        User user = new User();
        user.setUserName("user");
        user.setWallet(new Wallet());
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        walletService.deposit("user", requestModel);

        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void expectAuthenticationFailedInDeposit() {
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(Optional.empty());
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);

        assertThrows(AuthenticationFailedException.class, () -> {
            walletService.deposit("nonExistentUser", requestModel);
        });
    }

    @Test
    void expectInvalidMoneyExceptionWithNegativeAmountOnDeposit() {
        User user = new User();
        user.setUserName("user");
        user.setWallet(new Wallet());
        WalletRequestModel requestModel = new WalletRequestModel(-50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));

        assertThrows(InvalidAmountException.class, () -> walletService.deposit("user", requestModel));
    }

    @Test
    void expectWithdrawMoney() {
        Wallet wallet = new Wallet();
        wallet.deposit(new Money(100, Currency.RUPEE));
        User user = new User();
        user.setUserName("user");
        user.setWallet(wallet);
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        walletService.withdraw("user", requestModel);


        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void expectInvalidMoneyExceptionWithNegativeAmountInWithdraw() {
        User user = new User();
        user.setUserName("user");
        user.setWallet(new Wallet());
        WalletRequestModel requestModel = new WalletRequestModel(-50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));

        assertThrows(InvalidAmountException.class, () -> walletService.withdraw("user", requestModel));
    }

    @Test
    void expectInsufficientMoneyExceptionWithInsufficientFundsInWithdraw() {
        User user = new User();
        user.setUserName("user");
        user.setWallet(new Wallet());
        WalletRequestModel requestModel = new WalletRequestModel(200.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));

        assertThrows(InsufficientMoneyException.class, () -> walletService.withdraw("user", requestModel));
    }

    @Test
    void expectDepositDollars() {
        User user = new User();
        user.setUserName("user");
        user.setWallet(new Wallet());
        WalletRequestModel requestModel = new WalletRequestModel(1.0, Currency.DOLLAR);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        walletService.deposit("user", requestModel);

        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void expectWithdrawDollars() {
        Wallet wallet = new Wallet();
        wallet.deposit(new Money(100, Currency.RUPEE));
        User user = new User();
        user.setUserName("user");
        user.setWallet(wallet);
        WalletRequestModel requestModel = new WalletRequestModel(1.0, Currency.DOLLAR);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        walletService.withdraw("user", requestModel);


        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void expectGetAllWallets() {
        Wallet wallet1 = new Wallet(1, new Money());
        Wallet wallet2 = new Wallet(2, new Money());
        when(walletRepository.findAll()).thenReturn(Arrays.asList(wallet1, wallet2));
        List<WalletResponseModel> wallets = walletService.fetchWallets();

        assertEquals(2L, wallets.size());
    }

    @Test
    void expectTransactionSuccessful() {
        Money moneyForTransaction = new Money(100, Currency.RUPEE);
        Wallet sendersWallet = wallet;
        sendersWallet.deposit(new Money(1000, Currency.RUPEE));
        Wallet receiversWallet = wallet;

        walletService.transact(sendersWallet, receiversWallet, moneyForTransaction);

        verify(sendersWallet, times(1)).withdraw(moneyForTransaction);
        verify(receiversWallet, times(1)).deposit(moneyForTransaction);
    }
}
