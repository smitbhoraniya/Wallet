package com.swiggy.wallet.repositories;

import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByIdAndUser(int id, User user);
    List<Wallet> findAllByUser(User user);
}
