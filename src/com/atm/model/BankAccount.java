package com.atm.model;

import com.atm.controller.AtmController;
import com.atm.controller.AtmControllerImpl;

import java.util.UUID;

public class BankAccount {

    private final AtmController atmController = new AtmControllerImpl();

    private UUID accountId;
    private String accountName;
    private String accountPin;
    private double accountBalance;

    public BankAccount(String accountName, String accountPin, double accountBalance) {
        this.accountId = UUID.randomUUID();
        this.accountName = accountName;
        this.accountPin = accountPin;
        this.accountBalance = accountBalance;
    }

    public BankAccount() {
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPin() {
        return accountPin;
    }

    public void setAccountPin(String accountPin) {
        this.accountPin = accountPin;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", accountPin='" + accountPin + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }

    public void atmStart() {
        atmController.atmStart();
    }
}
