package com.ihbank.ihbank.models.accounts;


import com.ihbank.ihbank.models.users.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Savings extends Account{

    private BigDecimal interestRate = new BigDecimal("0.0025");


    private BigDecimal minimumBalance = new BigDecimal("1000.00");;

    @NotEmpty
    private String secretKey;

    private LocalDate interestDate = getCreationDate();

    public Savings() {
    }

    public Savings(BigDecimal balance, User primaryOwner, User secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        this.secretKey = secretKey;
    }

    public Savings(BigDecimal balance, User primaryOwner, User secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal newInterestRate) {
        if (newInterestRate.compareTo(new BigDecimal("0.0025")) < 0 || newInterestRate == null){
            this.interestRate = new BigDecimal("0.0025");
        }
        else if(newInterestRate.compareTo(new BigDecimal("0.50")) > 0 ){
            this.interestRate = new BigDecimal("0.50");
        }
        else {
            this.interestRate = newInterestRate;
        }
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

    public LocalDate getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(LocalDate interestDate) {
        this.interestDate = interestDate;
    }

    public void setMinimumBalance(BigDecimal newMinimumBalance) {
        if (newMinimumBalance.compareTo(new BigDecimal("100")) < 0){
            this.minimumBalance = new BigDecimal("100");
        }
        else if(newMinimumBalance.compareTo(new BigDecimal("1000")) > 0 || newMinimumBalance == null){
            this.minimumBalance = new BigDecimal("1000");
        }
        else {
            this.minimumBalance = newMinimumBalance;
        }
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.minimumBalance = new BigDecimal("1000.0");
        if (balance.compareTo(this.minimumBalance) < 0 ){
            super.setBalance(balance.subtract(getPenaltyFee()));
        }
        if (balance.compareTo(this.minimumBalance) > 0) {
            super.setBalance(balance);
        }
    }

    public void applyInterest(){
        if (LocalDate.now().getYear() > this.getInterestDate().getYear()) {
            for (int i = LocalDate.now().getYear(); i > this.getInterestDate().getYear() ; i--) {
                this.setBalance(this.getBalance().multiply(interestRate.add(BigDecimal.valueOf(1))));
            }
            setInterestDate(LocalDate.now());
        }
    }
}
