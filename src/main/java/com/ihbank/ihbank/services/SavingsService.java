package com.ihbank.ihbank.services;


import com.ihbank.ihbank.models.accounts.Savings;
import com.ihbank.ihbank.repositories.accounts.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsService {

    @Autowired
    SavingsRepository savingsRepository;


    public List<Savings> allSavingsAccounts() {
        return savingsRepository.findAll();
    }
}
