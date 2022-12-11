package com.ihbank.ihbank.models.accounts;


import com.ihbank.ihbank.models.users.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditCard extends Account{

    @NotNull
    private BigDecimal creditLimit = new BigDecimal("100.0");

    @NotNull
    private BigDecimal interestRate = new BigDecimal("0.2");

    private LocalDate interestDate = getCreationDate();

    public CreditCard() {
    }

    public CreditCard(BigDecimal balance, User primaryOwner, User secondaryOwner, BigDecimal creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCard(BigDecimal balance, User primaryOwner, User secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
    }

    public LocalDate getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(LocalDate interestDate) {
        this.interestDate = interestDate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }


    public void setCreditLimit(BigDecimal newCreditLimit) {
        if (newCreditLimit.compareTo(new BigDecimal("100")) < 0 || newCreditLimit == null){
            this.creditLimit = new BigDecimal("100");
        }
        else if(newCreditLimit.compareTo(new BigDecimal("100000")) > 0){
            this.creditLimit = new BigDecimal("100000");
        } else {
            this.creditLimit = newCreditLimit;
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal newInterestRate) {
        if (newInterestRate.compareTo(new BigDecimal("0.1")) < 0 ){
            this.interestRate = new BigDecimal("0.1");
        }
        if(newInterestRate.compareTo(new BigDecimal("0.2")) > 0 || newInterestRate == null){
            this.interestRate = new BigDecimal("0.2");
        }
    }

    public void applyInterest(){
        if (LocalDate.now().getMonthValue() > this.getInterestDate().getMonthValue() && LocalDate.now().getYear() >= this.getInterestDate().getYear()) {
            for (int i = LocalDate.now().getMonthValue(); i > this.getInterestDate().getMonthValue() ; i--) {
                this.setBalance(this.getBalance().multiply(interestRate.add(BigDecimal.valueOf(1))));
            }
            setInterestDate(LocalDate.now());
        }
    }
}
