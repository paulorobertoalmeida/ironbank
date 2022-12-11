package com.ihbank.ihbank.services;

import com.ihbank.ihbank.models.accounts.Checking;
import com.ihbank.ihbank.repositories.accounts.CheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckingService {

    @Autowired
    CheckingRepository checkingRepository;

    public List<Checking> allCheckingAccounts() {
        return checkingRepository.findAll();
    }


}
