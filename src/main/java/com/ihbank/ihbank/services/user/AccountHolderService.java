package com.ihbank.ihbank.services.user;


import com.ihbank.ihbank.dto.AccountHolderDTO;
import com.ihbank.ihbank.models.users.AccountHolder;
import com.ihbank.ihbank.models.users.Address;
import com.ihbank.ihbank.models.users.Role;
import com.ihbank.ihbank.repositories.users.AccountHolderRepository;
import com.ihbank.ihbank.repositories.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class AccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AccountHolder createAccountHolder(AccountHolderDTO accountHolderDTO) {
        if (LocalDate.now().getYear() - accountHolderDTO.getDateOfBirth().getYear() < 18) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Holder must be 18 years old at least.");
        }
        Address primaryAddress = new Address(accountHolderDTO.getStreetName(), accountHolderDTO.getBuildingNumber(), accountHolderDTO.getPostalCode(), accountHolderDTO.getCity(), accountHolderDTO.getCountry());
        Address mailingAddress = new Address(accountHolderDTO.getMailingStreetName(), accountHolderDTO.getMailingBuildingNumber(), accountHolderDTO.getMailingPostalCode(), accountHolderDTO.getMailingCity(), accountHolderDTO.getMailingCountry());
        AccountHolder accountHolder = new AccountHolder(accountHolderDTO.getUsername(), passwordEncoder.encode(accountHolderDTO.getPassword()), accountHolderDTO.getName(), accountHolderDTO.getDateOfBirth(), primaryAddress, mailingAddress);
        accountHolderRepository.save(accountHolder);
        Role holderRole = new Role ("ACCOUNT HOLDER", accountHolder);
        roleRepository.save(holderRole);
        accountHolderRepository.save(accountHolder);
        return accountHolder;
    }
}
