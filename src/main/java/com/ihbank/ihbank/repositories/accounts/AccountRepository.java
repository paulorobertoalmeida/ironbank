package com.ihbank.ihbank.repositories.accounts;

import com.ihbank.ihbank.models.accounts.Account;
import com.ihbank.ihbank.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAll();

    List<Account> findByPrimaryOwner(User user);
}