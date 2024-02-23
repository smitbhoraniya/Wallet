package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.enums.Country;
import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.InsufficientMoneyException;
import com.swiggy.wallet.execptions.SameUserTransactionException;
import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Transaction;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.Wallet;
import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.repositories.TransactionRepository;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.repositories.WalletRepository;
import com.swiggy.wallet.services.TransactionService;
import com.swiggy.wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class TransactionServiceTest {
    @Mock
    Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private WalletService walletService;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void expectTransactionSuccessful() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        sender.setUserId(1);
        Wallet senderWallet = spy(new Wallet(sender));
        senderWallet.setId(1);
        senderWallet.deposit(new Money(100, Currency.RUPEE));
        User receiver = new User("receiver", "receiverPassword", Country.INDIA);
        receiver.setUserId(2);
        Wallet receiverWallet = spy(new Wallet(receiver));
        receiverWallet.setId(2);
        Money money = new Money(100.0, Currency.RUPEE);
        TransactionRequestModel requestModel = new TransactionRequestModel("receiver", 1, 2, money);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));
        when(walletRepository.findByIdAndUser(1, sender)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByIdAndUser(2, receiver)).thenReturn(Optional.of(receiverWallet));

        TransactionResponseModel response = transactionService.transaction(requestModel);

        TransactionResponseModel expected = new TransactionResponseModel(
                "sender"
                , "receiver"
                , new Money(100, Currency.RUPEE)
                , response.getCreatedAt()
                , new Money(0, Currency.RUPEE)
        );
        verify(senderWallet, times(1)).withdraw(money);
        verify(receiverWallet, times(1)).deposit(money);
        verify(userRepository, times(2)).findByUserName(anyString());
        verify(walletRepository, times(2)).findByIdAndUser(anyInt(), any());
        assertEquals(expected, response);
    }

    @Test
    void expectSameAccountExceptionInSameAccountTransaction() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        Wallet senderWallet = new Wallet(sender);
        senderWallet.setId(1);
        TransactionRequestModel requestModel = new TransactionRequestModel("sender", 1, 1, new Money(100.0, Currency.RUPEE));
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));

        assertThrows(SameUserTransactionException.class, () -> transactionService.transaction(requestModel));
        verify(userRepository, times(0)).save(sender);
    }

    @Test
    void expectReceiverNotFoundOnTransaction() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        TransactionRequestModel requestModel = new TransactionRequestModel("receiver", 1, 2, new Money(100.0, Currency.RUPEE));
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transactionService.transaction(requestModel));
        verify(userRepository, times(2)).findByUserName(anyString());
    }

    @Test
    void expectFetchAllTransaction() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        User receiver = new User("receiver", "receiverPassword", Country.INDIA);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));
        Transaction transaction = new Transaction(sender, receiver, new Money(10, Currency.RUPEE));
        Transaction transaction1 = new Transaction(sender, receiver, new Money(10, Currency.RUPEE));
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction1);
        when(transactionRepository.findBySenderOrRecipientName(sender)).thenReturn(transactions);

        List<TransactionResponseModel> transactionResponseModelList = transactionService.fetchTransactions();

        assertEquals(2, transactionResponseModelList.size());
        verify(transactionRepository, times(1)).findBySenderOrRecipientName(sender);
        verify(userRepository, times(1)).findByUserName("sender");
        verify(transactionRepository, times(1)).findBySenderOrRecipientName(any());
    }

    @Test
    void expectEmptyListInAllTransaction() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));

        List<TransactionResponseModel> transactionResponseModelList = transactionService.fetchTransactions();

        assertEquals(0, transactionResponseModelList.size());
        verify(transactionRepository, times(1)).findBySenderOrRecipientName(sender);
        verify(userRepository, times(1)).findByUserName(anyString());
    }

    @Test
    void expectListOfTransactionBetweenDateTime() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        User receiver = new User("receiver", "receiverPassword", Country.INDIA);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));
        Transaction transaction = new Transaction(sender, receiver, new Money(10, Currency.RUPEE));
        transaction.setCreatedAt(LocalDateTime.now().minusMonths(12));
        LocalDateTime fromDateTime = LocalDateTime.now().minusHours(2);
        LocalDateTime toDateTime = LocalDateTime.now();
        when(transactionRepository.findBySenderOrRecipientNameAndDateTimeBefore(sender, fromDateTime, toDateTime)).thenReturn(List.of());

        List<TransactionResponseModel> transactionResponseModelList = transactionService.fetchTransactions(fromDateTime, toDateTime);

        assertEquals(0, transactionResponseModelList.size());
        verify(transactionRepository, times(1)).findBySenderOrRecipientNameAndDateTimeBefore(sender, fromDateTime, toDateTime);
        verify(userRepository, times(1)).findByUserName(anyString());
    }

    @Test
    void expectTransactionInDifferentWalletOfSameUser() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        sender.setUserId(1);
        Wallet senderWallet = spy(new Wallet(sender));
        senderWallet.deposit(new Money(150.0, Currency.RUPEE));
        senderWallet.setId(1);
        Wallet otherSenderWallet = spy(new Wallet(sender));
        senderWallet.setId(2);
        Money money = new Money(100.0, Currency.RUPEE);
        TransactionRequestModel requestModel = new TransactionRequestModel("sender", 1, 2, money);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(walletRepository.findByIdAndUser(1, sender)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByIdAndUser(2, sender)).thenReturn(Optional.of(otherSenderWallet));

        TransactionResponseModel response = transactionService.transaction(requestModel);

        TransactionResponseModel expected = new TransactionResponseModel(
                "sender"
                , "sender"
                , new Money(100, Currency.RUPEE)
                , response.getCreatedAt()
                , new Money(0, Currency.RUPEE)
        );
        verify(senderWallet, times(1)).withdraw(money);
        verify(otherSenderWallet, times(1)).deposit(money);
        verify(userRepository, times(2)).findByUserName(anyString());
        verify(walletRepository, times(2)).findByIdAndUser(anyInt(), any());
        assertEquals(expected, response);
    }

    @Test
    void expectServiceChargeInTransfer() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        sender.setUserId(1);
        Wallet senderWallet = spy(new Wallet(sender));
        senderWallet.setId(1);
        senderWallet.deposit(new Money(100, Currency.RUPEE));
        User receiver = new User("receiver", "receiverPassword", Country.AMERICA);
        receiver.setUserId(2);
        Wallet receiverWallet = spy(new Wallet(receiver));
        receiverWallet.setId(2);
        Money money = new Money(100.0, Currency.RUPEE);
        TransactionRequestModel requestModel = new TransactionRequestModel("receiver", 1, 2, money);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));
        when(walletRepository.findByIdAndUser(1, sender)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByIdAndUser(2, receiver)).thenReturn(Optional.of(receiverWallet));

        TransactionResponseModel response = transactionService.transaction(requestModel);

        TransactionResponseModel expected = new TransactionResponseModel("sender", "receiver", new Money(90, Currency.RUPEE), response.getCreatedAt(), new Money(10, Currency.RUPEE));
        verify(senderWallet, times(1)).withdraw(money);
        verify(receiverWallet, times(1)).deposit(money);
        verify(userRepository, times(2)).findByUserName(anyString());
        verify(walletRepository, times(2)).findByIdAndUser(anyInt(), any());
        assertEquals(expected, response);
    }

    @Test
    void expectServiceChargeIsMoreThanTransfer() {
        User sender = new User("sender", "senderPassword", Country.INDIA);
        sender.setUserId(1);
        Wallet senderWallet = spy(new Wallet(sender));
        senderWallet.setId(1);
        senderWallet.deposit(new Money(100, Currency.RUPEE));
        User receiver = new User("receiver", "receiverPassword", Country.AMERICA);
        receiver.setUserId(2);
        Wallet receiverWallet = spy(new Wallet(receiver));
        receiverWallet.setId(2);
        Money money = new Money(9.0, Currency.RUPEE);
        TransactionRequestModel requestModel = new TransactionRequestModel("receiver", 1, 2, money);
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));
        when(walletRepository.findByIdAndUser(1, sender)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByIdAndUser(2, receiver)).thenReturn(Optional.of(receiverWallet));

        assertThrows(InsufficientMoneyException.class, () -> transactionService.transaction(requestModel));

        verify(senderWallet, times(1)).withdraw(money);
        verify(receiverWallet, times(1)).deposit(money);
        verify(userRepository, times(2)).findByUserName(anyString());
        verify(walletRepository, times(2)).findByIdAndUser(anyInt(), any());
    }
}
