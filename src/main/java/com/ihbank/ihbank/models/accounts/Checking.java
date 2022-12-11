package com.ihbank.ihbank.models.accounts;


import com.ihbank.ihbank.models.users.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Checking extends Account{

    private BigDecimal minimumBalance = new BigDecimal("250.00");

    @NotEmpty
    private String secretKey;

    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12.00");

    private LocalDate MaintenanceDate = this.getCreationDate();

    public Checking() {
    }

    public Checking(BigDecimal balance, User primaryOwner, User secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.minimumBalance = new BigDecimal("250.00");
        this.monthlyMaintenanceFee = new BigDecimal("12.00");
        this.secretKey = secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LocalDate getMaintenanceDate() {
        return MaintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        MaintenanceDate = maintenanceDate;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.minimumBalance = new BigDecimal("250.00");
        if (balance.compareTo(this.minimumBalance) < 0 ){
            super.setBalance(balance.subtract(getPenaltyFee()));
        }
        else if (balance.compareTo(this.minimumBalance) > 0) {
            super.setBalance(balance);
        }
    }
}
