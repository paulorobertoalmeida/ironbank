package com.ihbank.ihbank.dto;


import java.time.LocalDate;

public class AccountHolderDTO {

    private String username;
    private String password;
    private String name;
    private LocalDate dateOfBirth;
    private String streetName;
    private Integer buildingNumber;
    private Integer postalCode;
    private String city;
    private String country;
    private String mailingStreetName;
    private Integer mailingBuildingNumber;
    private Integer mailingPostalCode;
    private String mailingCity;
    private String mailingCountry;

    public AccountHolderDTO() {
    }

    public AccountHolderDTO(String username, String password, String name, LocalDate dateOfBirth, String streetName, Integer buildingNumber, Integer postalCode, String city, String country, String mailingStreetName, Integer mailingBuildingNumber, Integer mailingPostalCode, String mailingCity, String mailingCountry) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.mailingStreetName = mailingStreetName;
        this.mailingBuildingNumber = mailingBuildingNumber;
        this.mailingPostalCode = mailingPostalCode;
        this.mailingCity = mailingCity;
        this.mailingCountry = mailingCountry;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMailingStreetName() {
        return mailingStreetName;
    }

    public void setMailingStreetName(String mailingStreetName) {
        this.mailingStreetName = mailingStreetName;
    }

    public Integer getMailingBuildingNumber() {
        return mailingBuildingNumber;
    }

    public void setMailingBuildingNumber(Integer mailingBuildingNumber) {
        this.mailingBuildingNumber = mailingBuildingNumber;
    }

    public Integer getMailingPostalCode() {
        return mailingPostalCode;
    }

    public void setMailingPostalCode(Integer mailingPostalCode) {
        this.mailingPostalCode = mailingPostalCode;
    }

    public String getMailingCity() {
        return mailingCity;
    }

    public void setMailingCity(String mailingCity) {
        this.mailingCity = mailingCity;
    }

    public String getMailingCountry() {
        return mailingCountry;
    }

    public void setMailingCountry(String mailingCountry) {
        this.mailingCountry = mailingCountry;
    }
}
