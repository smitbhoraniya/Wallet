package com.swiggy.wallet.repositories;

import com.swiggy.wallet.models.IntraWalletTransaction;
import com.swiggy.wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IntraWalletTransactionRepository extends JpaRepository<IntraWalletTransaction, Integer> {
    @Query("SELECT iwt FROM IntraWalletTransaction iwt INNER JOIN wallet w ON iwt.wallet = w WHERE w.user = ?1")
    List<IntraWalletTransaction> findAllByUserId(User user);

    @Query("SELECT iwt FROM IntraWalletTransaction iwt INNER JOIN wallet w ON iwt.wallet = w WHERE w.user = ?1 AND iwt.createdAt BETWEEN ?2 AND ?3")
    List<IntraWalletTransaction> findAllByUserAndCreatedAt(User user, LocalDateTime fromDateTime, LocalDateTime toDateTime);
}