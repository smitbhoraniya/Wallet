package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.execptions.SameUserTransactionException;
import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.Money;
import com.swiggy.wallet.models.Transaction;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.TransactionRequestModel;
import com.swiggy.wallet.models.responseModels.TransactionResponseModel;
import com.swiggy.wallet.repositories.TransactionRepository;
import com.swiggy.wallet.repositories.UserRepository;
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
        User sender = new User("sender", "senderPassword");
        sender.setUserId(1);
        User receiver = new User("receiver", "receiverPassword");
        receiver.setUserId(2);
        TransactionRequestModel requestModel = new TransactionRequestModel("receiver", new Money(100.0, Currency.RUPEE));
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));

        transactionService.transaction(requestModel);

        verify(walletService, times(1)).transact(sender.getWallet(), receiver.getWallet(), requestModel.getMoney());
        verify(userRepository, times(1)).save(sender);
        verify(userRepository, times(1)).save(receiver);
    }

    @Test
    void expectSameAccountExceptionInSameAccountTransaction() {
        User sender = new User("sender", "senderPassword");
        User receiver = new User("receiver", "receiverPassword");
        TransactionRequestModel requestModel = new TransactionRequestModel("sender", new Money(100.0, Currency.RUPEE));
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));

        assertThrows(SameUserTransactionException.class, () -> transactionService.transaction(requestModel));

        verify(walletService, times(0)).transact(sender.getWallet(), receiver.getWallet(), requestModel.getMoney());
        verify(userRepository, times(0)).save(sender);
        verify(userRepository, times(0)).save(receiver);
    }

    @Test
    void expectReceiverNotFoundOnTransaction() {
        User sender = new User("sender", "senderPassword");
        User receiver = new User("receiver", "receiverPassword");
        TransactionRequestModel requestModel = new TransactionRequestModel("receiver", new Money(100.0, Currency.RUPEE));
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transactionService.transaction(requestModel));
        verify(walletService, times(0)).transact(sender.getWallet(), receiver.getWallet(), requestModel.getMoney());
        verify(userRepository, times(0)).save(sender);
        verify(userRepository, times(0)).save(receiver);
    }

    @Test
    void expectFetchAllTransaction() {
        User sender = new User("sender", "senderPassword");
        User receiver = new User("receiver", "receiverPassword");
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
    }

    @Test
    void expectEmptyListInAllTransaction() {
        User sender = new User("sender", "senderPassword");
        User receiver = new User("receiver", "receiverPassword");
        when(authentication.getName()).thenReturn("sender");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("receiver")).thenReturn(Optional.of(receiver));

        List<TransactionResponseModel> transactionResponseModelList = transactionService.fetchTransactions();

        assertEquals(0, transactionResponseModelList.size());
        verify(transactionRepository, times(1)).findBySenderOrRecipientName(sender);
    }

    @Test
    void expectListOfTransactionBeforeDateTime() {
        User sender = new User("sender", "senderPassword");
        User receiver = new User("receiver", "receiverPassword");
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
    }
}
