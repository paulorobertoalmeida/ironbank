package com.ihbank.ihbank.repositories.users;

import com.ihbank.ihbank.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

    Optional<AccountHolder> findByName(String name);

    Optional<AccountHolder> findByUsername(String username);
}