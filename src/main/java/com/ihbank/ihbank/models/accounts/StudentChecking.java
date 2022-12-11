package com.ihbank.ihbank.models.accounts;


import com.ihbank.ihbank.models.users.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

@Entity

public class StudentChecking extends Account{

    @NotEmpty
    private String secretKey;

    public StudentChecking() {
    }

    public StudentChecking(BigDecimal balance, User primaryOwner, User secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
