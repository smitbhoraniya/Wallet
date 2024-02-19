package com.swiggy.wallet.repositories;

import com.swiggy.wallet.models.Transaction;
import com.swiggy.wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t where t.sender = ?1 or t.receiver = ?1")
    List<Transaction> findBySenderOrRecipientName(User user);

    @Query("SELECT t FROM Transaction t WHERE (t.sender = ?1 OR t.receiver = ?1) " +
            "AND t.createdAt BETWEEN ?2 AND ?3")
    List<Transaction> findBySenderOrRecipientNameAndDateTimeBefore(User user, LocalDateTime fromDateTime, LocalDateTime toDateTime);
}
