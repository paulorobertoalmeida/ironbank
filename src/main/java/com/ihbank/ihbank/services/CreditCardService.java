package com.ihbank.ihbank.services;


import com.ihbank.ihbank.models.accounts.CreditCard;
import com.ihbank.ihbank.repositories.accounts.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    public List<CreditCard> allCreditCardAccounts() {
        return creditCardRepository.findAll();
    }
}
