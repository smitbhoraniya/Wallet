package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.enums.Country;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.AuthenticationFailedException;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.InvalidAmountException;
import com.swiggy.wallet.execptions.WalletNotFoundException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.WalletRequestModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.repositories.IntraWalletTransactionRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private IntraWalletTransactionRepository intraWalletTransactionRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void expectDepositMoney() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(user);
        wallet1.setId(1);
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet1));
        WalletResponseModel expected = new WalletResponseModel(1, new Money(50, Currency.RUPEE));

        WalletResponseModel response = walletService.deposit(1, "user", requestModel);

        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
        verify(intraWalletTransactionRepository, times(1)).save(any());
        assertEquals(expected, response);
    }

    @Test
    void expectAuthenticationFailedInDeposit() {
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(Optional.empty());
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);

        assertThrows(AuthenticationFailedException.class, () -> {
            walletService.deposit(1, "nonExistentUser", requestModel);
        });
        verify(userRepository, times(1)).findByUserName(anyString());
    }

    @Test
    void expectInvalidMoneyExceptionWithNegativeAmountOnDeposit() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(user);
        wallet1.setId(1);
        WalletRequestModel requestModel = new WalletRequestModel(-50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet1));

        assertThrows(InvalidAmountException.class, () -> walletService.deposit(1, "user", requestModel));
        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName("user");
    }

    @Test
    void expectWalletNotMatch() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(user);
        wallet1.setId(1);
        wallet1.setId(2);
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.deposit(1, "user", requestModel));
        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, never()).save(any());
    }

    @Test
    void expectWithdrawMoney() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet = new Wallet(user);
        wallet.setId(1);
        wallet.deposit(new Money(100, Currency.RUPEE));
        WalletRequestModel requestModel = new WalletRequestModel(50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet));
        WalletResponseModel expected = new WalletResponseModel(1, new Money(50, Currency.RUPEE));

        WalletResponseModel response = walletService.withdraw(1, "user", requestModel);

        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
        verify(intraWalletTransactionRepository, times(1)).save(any());
        assertEquals(expected, response);
    }

    @Test
    void expectInvalidMoneyExceptionWithNegativeAmountInWithdraw() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(user);
        wallet1.setId(1);
        WalletRequestModel requestModel = new WalletRequestModel(-50.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidAmountException.class, () -> walletService.withdraw(1, "user", requestModel));
        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName(anyString());
    }

    @Test
    void expectInsufficientMoneyExceptionWithInsufficientFundsInWithdraw() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(user);
        wallet1.setId(1);
        WalletRequestModel requestModel = new WalletRequestModel(200.0, Currency.RUPEE);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet1));

        assertThrows(InsufficientMoneyException.class, () -> walletService.withdraw(1, "user", requestModel));
        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName(anyString());
    }

    @Test
    void expectDepositDollars() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(user);
        wallet1.setId(1);
        WalletRequestModel requestModel = new WalletRequestModel(1.0, Currency.DOLLAR);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet1));
        WalletResponseModel expected = new WalletResponseModel(1, new Money(80, Currency.RUPEE));

        WalletResponseModel response = walletService.deposit(1, "user", requestModel);

        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
        verify(intraWalletTransactionRepository, times(1)).save(any());
        assertEquals(expected, response);
    }

    @Test
    void expectWithdrawDollars() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet = new Wallet(user);
        wallet.setId(1);
        when(walletRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(wallet));
        wallet.deposit(new Money(100, Currency.RUPEE));
        WalletRequestModel requestModel = new WalletRequestModel(1.0, Currency.DOLLAR);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        WalletResponseModel expected = new WalletResponseModel(1, new Money(20, Currency.RUPEE));

        WalletResponseModel response = walletService.withdraw(1, "user", requestModel);

        verify(walletRepository, times(1)).findByIdAndUser(1, user);
        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).save(any());
        verify(intraWalletTransactionRepository, times(1)).save(any());
        assertEquals(expected, response);
    }

    @Test
    void expectGetAllWallets() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet1 = new Wallet(1, new Money(), user);
        Wallet wallet2 = new Wallet(2, new Money(), user);
        when(walletRepository.findAllByUser(user)).thenReturn(Arrays.asList(wallet1, wallet2));
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        List<WalletResponseModel> wallets = walletService.fetchWallets("user");

        assertEquals(2L, wallets.size());
        verify(userRepository, times(1)).findByUserName("user");
        verify(walletRepository, times(1)).findAllByUser(any());
    }

    @Test
    void expectWalletCreated() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet = new Wallet(user);
        wallet.setId(1);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));
        when(walletRepository.save(any())).thenReturn(wallet);
        WalletResponseModel expected = new WalletResponseModel(1, new Money(0, Currency.RUPEE));

        WalletResponseModel response = walletService.create("user");

        verify(userRepository, times(1)).findByUserName("user");
        verify(walletRepository, times(1)).save(any());
        assertEquals(expected, response);
    }

    @Test
    void expectAuthenticationFailInWalletCreated() {
        when(userRepository.findByUserName("notExist")).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> walletService.create("user"));
        verify(userRepository, times(1)).findByUserName("user");
    }
}
