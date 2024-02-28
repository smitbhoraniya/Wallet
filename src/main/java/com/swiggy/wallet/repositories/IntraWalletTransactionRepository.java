package com.swiggy.wallet.repositories;

import com.swiggy.wallet.models.IntraWalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntraWalletTransactionRepository extends JpaRepository<IntraWalletTransaction, Integer> {
}