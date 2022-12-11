package com.ihbank.ihbank.controllers.accounts;

import com.ihbank.ihbank.dto.AccountDTO;
import com.ihbank.ihbank.models.accounts.Account;
import com.ihbank.ihbank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {


    @Autowired
    AccountService accountService;

    @PostMapping("/create-account-one-holder")   // solo admin -- OK
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody AccountDTO accountDTO, @RequestParam Long holderId) {
        return accountService.createAccount(accountDTO, holderId);
    }


    @PostMapping("/create-account-two-holders")   // solo admin -- OK
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccountTwoHolders(@RequestBody AccountDTO accountDTO, @RequestParam Long holderId, @RequestParam Long secondaryHolderId) {
        return accountService.createAccountTwoHolders(accountDTO, holderId, secondaryHolderId);
    }

    @GetMapping("/all-accounts")   // solo admin -- OK
    @ResponseStatus(HttpStatus.OK)
    public List<Account> allAccounts() {
        return accountService.allAccounts();
    }


    @GetMapping("/admin-check-balance")  // solo admin -- OK
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal checkBalanceAdmin(@RequestParam Long id) {
        return accountService.checkBalanceAdmin(id);
    }

    @PatchMapping("/admin-change-balance")  // solo admin -- OK
    @ResponseStatus(HttpStatus.OK)
    public Account changeBalanceAdmin(@RequestParam Long accountId, BigDecimal newBalance) {
        return accountService.changeBalanceAdmin(accountId, newBalance);
    }

    @DeleteMapping("/delete-account/{id}")  // solo admin -- OK
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteAccount(@PathVariable Long id){
        return accountService.deleteAccount(id);
    }


    @PutMapping("/transfer") // solo admin -- OK
    @ResponseStatus(HttpStatus.OK)
    public String transferMoney(@RequestParam String senderName,
                                @RequestParam Long senderId,
                                @RequestParam BigDecimal amount,
                                @RequestParam Long receiveId){
        return accountService.transfer(senderName, senderId, amount, receiveId);
    }

    @GetMapping("/my-balance") // solo account holders -- OK
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal checkMyBalance(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long accountId) {
        return accountService.checkMyBalance(userDetails.getUsername(), accountId);
    }


    @PutMapping("/new-transfer")  // solo account holder -- OK
    @ResponseStatus(HttpStatus.OK)
    public String transferMoney(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam Long sendingId,
                                @RequestParam BigDecimal amount,
                                @RequestParam Long receiveId){
        return accountService.newTransfer(userDetails, sendingId, amount, receiveId);
    }

    @PutMapping("/third-party-transfer")  // solo third party -- OK
    @ResponseStatus(HttpStatus.OK)
    public String transferThirdParty(@RequestHeader String hashedKey,
                                     @RequestParam Long sendingAccountId,
                                     @RequestParam BigDecimal amount,
                                     @RequestParam Long receiveAccountId,
                                     @RequestParam String secretKeySendingAccount){
        return accountService.transferThirdParty(hashedKey, sendingAccountId, amount, receiveAccountId, secretKeySendingAccount);
    }
}
