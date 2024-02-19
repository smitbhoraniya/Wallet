package com.swiggy.wallet.repositories;

import com.swiggy.wallet.models.Transaction;
import com.swiggy.wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t where t.sender = ?1 or t.receiver = ?1")
    public List<Transaction> findBySenderOrRecipientName(User user);
}
