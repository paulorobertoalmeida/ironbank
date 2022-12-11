package com.ihbank.ihbank.dto;


import java.math.BigDecimal;

public class AccountDTO {

    private Long accountId;

    private BigDecimal balance;
    private String secretKey;
    private BigDecimal minimumBalance;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;
    private String typeOfAccount;

    public AccountDTO() {
    }

    public AccountDTO(BigDecimal balance, String secretKey, String typeOfAccount) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.typeOfAccount = typeOfAccount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }
}
