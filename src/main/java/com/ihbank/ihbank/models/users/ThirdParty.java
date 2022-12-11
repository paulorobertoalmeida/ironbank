package com.ihbank.ihbank.models.users;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ThirdParty extends User{

    private String name;
    @Column(unique = true)
    private String hashedKey;

    public ThirdParty() {
    }

    public ThirdParty(String username, String password, String name, String hashedKey) {
        super(username, password);
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
