package com.ihbank.ihbank.services;


import com.ihbank.ihbank.dto.AccountDTO;
import com.ihbank.ihbank.models.accounts.*;
import com.ihbank.ihbank.models.users.AccountHolder;
import com.ihbank.ihbank.repositories.accounts.*;
import com.ihbank.ihbank.repositories.users.AccountHolderRepository;
import com.ihbank.ihbank.repositories.users.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {


    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public List<Account> allAccounts() {
        return accountRepository.findAll();
    }


    public BigDecimal checkBalanceAdmin(Long id) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This account doesn't exist.");
        }
        Account account = accountRepository.findById(id).get();
        return account.getBalance();
    }

    public BigDecimal checkMyBalance(String username, Long accountId) {
        if (accountHolderRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This account doesn't exist.");
        }
        AccountHolder holder = accountHolderRepository.findByUsername(username).get();
        ApplyInterest(holder.getPrimaryAccounts(), holder.getSecondaryAccounts());
        for (Account prim : holder.getPrimaryAccounts()) {
            if (prim.getAccountId() == accountId) {
                Account accountOrigin = accountRepository.findById(accountId).get();
                return accountOrigin.getBalance();
            }
        }

        for (Account sec : holder.getSecondaryAccounts()) {
            if (sec.getAccountId() == accountId) {
                Account accountOrigin = accountRepository.findById(accountId).get();
                return accountOrigin.getBalance();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The selected ID doesn't match any primary nor secondary account from the selected user.");
    }


    public String deleteAccount(Long id) {
        if (accountRepository.findById(id).isPresent()) {
            accountRepository.deleteById(id);
            return "Account " + id + " has been deleted.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The account does not exist");        }
    }

    public Account changeBalanceAdmin(Long accountId, BigDecimal newBalance) {
        if (newBalance.compareTo(new BigDecimal("40")) < 0){ //40 because it is the penalty fee. In case we put a smaller balance the result would be negative.
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New balance can't be negative.");
        }
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "An account with this ID doesn't exist.");
        }
        Account account = accountRepository.findById(accountId).get();
        account.setBalance(newBalance);
        accountRepository.save(account);
        return account;
    }

    //method for applying interest. It is called when an account holder checks his balance. (endpoint "/my-balance")
    public void ApplyInterest(List<Account> primaryAccounts, List<Account> secondaryAccounts) {
        for (Account primary : primaryAccounts) {
            if (primary instanceof Savings) {
                ((Savings) primary).applyInterest();
            } else if (primary instanceof CreditCard) {
                ((CreditCard) primary).applyInterest();
            }
        }
        for (Account secondary : secondaryAccounts) {
            if (secondary instanceof Savings) {
                ((Savings) secondary).applyInterest();
            } else if (secondary instanceof CreditCard) {
                ((CreditCard) secondary).applyInterest();
            }
        }
    }

    //this transfer is only doable by admins, because it doesn't ask for a username (only regular name)
    public String transfer(String sendingName, Long sendingId, BigDecimal amount, Long receiveId) {
        if (amount.compareTo(new BigDecimal("0")) < 0 || amount.compareTo(new BigDecimal("0")) == 0 ){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Amount to transfer has to be grater than 0");
        } // you can't transfer 0 or less.

        if (accountHolderRepository.findByName(sendingName).isPresent()) {
            if (accountRepository.findById(receiveId).isPresent()) {
                AccountHolder sender = accountHolderRepository.findByName(sendingName).get();
                Account recipientAccount = accountRepository.findById(receiveId).get();

                for (Account prim : sender.getPrimaryAccounts()) {
                    if (prim.getAccountId() == sendingId) {
                        Account accountOrigin = accountRepository.findById(sendingId).get();
                        if (accountOrigin.getBalance().compareTo(amount) < 0) {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Your balance is not enough!");
                        } else {

                            accountOrigin.setBalance(accountOrigin.getBalance().subtract(amount));
                            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

                            accountRepository.saveAll(List.of(accountOrigin, recipientAccount));
                            return "You have transferred " + amount + "€ from account " + sendingId + ", owned by " + sendingName + ", to account " + receiveId;
                        }
                    }
                }
                if (sender.getSecondaryAccounts().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The selected ID doesn't match any primary nor secondary account from the selected user.");
                }

                for (Account sec : sender.getSecondaryAccounts()) {
                    if (sec.getAccountId() == sendingId) {
                        Account accountOrigin = accountRepository.findById(sendingId).get();
                        if (accountOrigin.getBalance().compareTo(amount) < 0) {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Your balance is not enough!");
                        } else {

                            accountOrigin.setBalance(accountOrigin.getBalance().subtract(amount));
                            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

                            accountRepository.saveAll(List.of(accountOrigin, recipientAccount));
                            return "You have transferred " + amount + "€ from account " + sendingId + ", secondarily owned by " + sendingName + ", to account " + receiveId;
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The selected ID doesn't match any primary nor secondary account from the selected user.");
                    }
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This receiving ID doesn't exist.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This sending user doesn't exist.");
        }
        return "";
    }



    //transfer available for account holders. It needs holder's autentication and can only use his accounts.
    public String newTransfer(UserDetails userDetails, Long sendingId, BigDecimal amount, Long receiveId) {
        if (amount.compareTo(new BigDecimal("0")) < 0 || amount.compareTo(new BigDecimal("0")) == 0 ){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Amount to transfer has to be grater than 0");
        }

        if (accountHolderRepository.findByUsername(userDetails.getUsername()).isPresent()) {
            if (accountRepository.findById(receiveId).isPresent()) {
                AccountHolder sender = accountHolderRepository.findByUsername(userDetails.getUsername()).get();
                Account recipientAccount = accountRepository.findById(receiveId).get();

                for (Account prim : sender.getPrimaryAccounts()) {
                    if (prim.getAccountId() == sendingId) {
                        Account accountOrigin = accountRepository.findById(sendingId).get();
                        if (accountOrigin.getBalance().compareTo(amount) < 0) {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Your balance is not enough!");
                        } else {

                            accountOrigin.setBalance(accountOrigin.getBalance().subtract(amount));
                            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

                            accountRepository.saveAll(List.of(accountOrigin, recipientAccount));
                            return "You have transferred " + amount + "€ from account " + sendingId + ", owned by " + sender.getName() + ", to account " + receiveId;
                        }
                    }
                }
                if (sender.getSecondaryAccounts().isEmpty())
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The selected ID doesn't match any primary nor secondary account from the selected user.");


                for (Account sec : sender.getSecondaryAccounts()) {
                    if (sec.getAccountId() == sendingId) {
                        Account accountOrigin = accountRepository.findById(sendingId).get();
                        if (accountOrigin.getBalance().compareTo(amount) < 0) {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Your balance is not enough!");
                        } else {

                            accountOrigin.setBalance(accountOrigin.getBalance().subtract(amount));
                            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

                            accountRepository.saveAll(List.of(accountOrigin, recipientAccount));
                            return "You have transferred " + amount + "€ from account " + sendingId + ", secondarily owned by " + sender.getName() + ", to account " + receiveId;
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The selected ID doesn't match any primary nor secondary account from the selected user.");
                    }
                }


            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This receiving ID doesn't exist.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This sending user doesn't exist.");
        }
        return "";
    }


    public Account createAccount(AccountDTO accountDTO, Long accountHolderId) {
        if (accountHolderRepository.findById(accountHolderId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This account holder ID doesn't exist. Please select an existing ID.");
        }
        AccountHolder holder = accountHolderRepository.findById(accountHolderId).get();
        if ((LocalDate.now().getYear() - holder.getDateOfBirth().getYear() < 18)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Holder must be 18 years old at least.");
        }
        switch (accountDTO.getTypeOfAccount().trim().toLowerCase()) {
            case "savings" -> {
                Savings account = new Savings(accountDTO.getBalance(), holder, null, accountDTO.getSecretKey());
                savingsRepository.save(account);
                return account;
            }
            case "creditcard" -> {
                CreditCard account = new CreditCard(accountDTO.getBalance(), holder, null);
                creditCardRepository.save(account);
                return account;
            }
            case "checking" -> {
                LocalDate dateOfBirth = holder.getDateOfBirth();
                if (LocalDate.now().getYear() - dateOfBirth.getYear() <= 24) {
                    StudentChecking account = new StudentChecking(accountDTO.getBalance(), holder, null, accountDTO.getSecretKey());
                    studentCheckingRepository.save(account);
                    return account;
                } else {
                    Checking account = new Checking(accountDTO.getBalance(), holder, null, accountDTO.getSecretKey());
                    checkingRepository.save(account);
                    return account;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account type selected doesn't exist. Please choose one between Checking, Savings or CreditCard.");
    }


    public Account createAccountTwoHolders(AccountDTO accountDTO, Long accountHolderId, Long secondaryHolderId) {
        if (accountHolderRepository.findById(accountHolderId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This account holder ID doesn't exist. Please select an existing ID.");
        }
        if (accountHolderRepository.findById(secondaryHolderId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This secondary holder ID doesn't exist. Please select an existing ID.");
        }

        AccountHolder holder = accountHolderRepository.findById(accountHolderId).get();
        AccountHolder secondary = accountHolderRepository.findById(secondaryHolderId).get();

        if ((LocalDate.now().getYear() - holder.getDateOfBirth().getYear() < 18)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Holder must be 18 years old at least.");
        }

        switch (accountDTO.getTypeOfAccount().trim().toLowerCase()) {
            case "savings" -> {
                Savings account = new Savings(accountDTO.getBalance(), holder, secondary, accountDTO.getSecretKey());
                savingsRepository.save(account);
                return account;
            }
            case "creditcard" -> {
                CreditCard account = new CreditCard(accountDTO.getBalance(), holder, secondary);
                creditCardRepository.save(account);
                return account;
            }
            case "checking" -> {
                LocalDate dateOfBirth = holder.getDateOfBirth();
                if (LocalDate.now().getYear() - dateOfBirth.getYear() <= 24) {
                    StudentChecking account = new StudentChecking(accountDTO.getBalance(), holder, secondary, accountDTO.getSecretKey());
                    studentCheckingRepository.save(account);
                    return account;
                } else {
                    Checking account = new Checking(accountDTO.getBalance(), holder, secondary, accountDTO.getSecretKey());
                    checkingRepository.save(account);
                    return account;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account type selected doesn't exist. Please choose one between Checking, Savings or CreditCard.");
    }



    //transfer available only for third parties. It asks for third party's hashed key and the secret key of the account that will transfer the money.
    public String transferThirdParty(String hashedKey, Long sendingAccountID, BigDecimal amount, Long receiveAccountId, String secretKeySendingAccount) {
        if (!thirdPartyRepository.findByHashedKey(hashedKey).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The hashed key is not correct.");
        }
        if (!accountRepository.findById(sendingAccountID).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The sender account ID is not correct.");
        }
        if (!accountRepository.findById(receiveAccountId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The receiver account ID is not correct.");
        }
        if (amount.compareTo(new BigDecimal("0")) < 0 || amount.compareTo(new BigDecimal("0")) == 0 ){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Amount to transfer has to be grater than 0");
        }

        Account senderAccount = accountRepository.findById(sendingAccountID).get();
        Account receiveAccount = accountRepository.findById(receiveAccountId).get();
        if (senderAccount instanceof CreditCard) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Third Parties can't transfer from CreditCard accounts.");
        } else if (senderAccount instanceof Checking) {
            if (((Checking) senderAccount).getSecretKey().equals(secretKeySendingAccount)) {
                if (senderAccount.getBalance().compareTo(amount) < 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Balance in sender account is not enough!");
                } else {

                    senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
                    receiveAccount.setBalance(receiveAccount.getBalance().add(amount));

                    accountRepository.saveAll(List.of(senderAccount, receiveAccount));
                    return "You have transferred " + amount + "€ from account " + sendingAccountID + " to account " + receiveAccountId;

                }
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Secret key from sender account is not correct!");
            }
        } else if (senderAccount instanceof Savings) {
            if (((Savings) senderAccount).getSecretKey().equals(secretKeySendingAccount)) {
                if (senderAccount.getBalance().compareTo(amount) < 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Balance in sender account is not enough!");
                } else {

                    senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
                    receiveAccount.setBalance(receiveAccount.getBalance().add(amount));

                    accountRepository.saveAll(List.of(senderAccount, receiveAccount));
                    return "You have transferred " + amount + "€ from account " + sendingAccountID + " to account " + receiveAccountId;
                }
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Secret key from sender account is not correct!");
            }
        } else if (senderAccount instanceof StudentChecking) {
            if (((StudentChecking) senderAccount).getSecretKey().equals(secretKeySendingAccount)) {
                if (senderAccount.getBalance().compareTo(amount) < 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Balance in sender account is not enough!");
                } else {

                    senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
                    receiveAccount.setBalance(receiveAccount.getBalance().add(amount));

                    accountRepository.saveAll(List.of(senderAccount, receiveAccount));
                    return "You have transferred " + amount + "€ from account " + sendingAccountID + " to account " + receiveAccountId;
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Secret key from sender account is not correct!");
            }
        }
        return "";
    }



}
