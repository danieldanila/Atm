package com.atm.model;

public abstract class Transaction {

    private double accountBalanceBeforeTransaction;
    private double accountBalanceAfterTransaction;
    private double accountBalanceDifference;
    private boolean transactionStatus;

    public Transaction(double accountBalanceBeforeTransaction, double accountBalanceAfterTransaction, double accountBalanceDifference, boolean transactionStatus) {
        this.accountBalanceBeforeTransaction = accountBalanceBeforeTransaction;
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
        this.accountBalanceDifference = accountBalanceDifference;
        this.transactionStatus = transactionStatus;
    }

    public abstract void transaction();

    public double getAccountBalanceBeforeTransaction() {
        return accountBalanceBeforeTransaction;
    }

    public double getAccountBalanceAfterTransaction() {
        return accountBalanceAfterTransaction;
    }

    public double getAccountBalanceDifference() {
        return accountBalanceDifference;
    }

    public boolean isTransactionStatus() {
        return transactionStatus;
    }
}
