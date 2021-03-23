package com.atm.model;

public class Deposit extends Transaction {

    public Deposit(double accountBalanceBeforeTransaction, double accountBalanceAfterTransaction, double accountBalanceDifference, boolean depositStatus) {
        super(accountBalanceBeforeTransaction, accountBalanceAfterTransaction, accountBalanceDifference, depositStatus);
    }

    @Override
    public void transaction() {
        System.out.println("The account balance before the deposit: " + getAccountBalanceBeforeTransaction()
                + "\nThe account balance after the deposit: " + getAccountBalanceAfterTransaction()
                + "\nThe account balance difference: " + getAccountBalanceDifference()
                + "\nThe deposit status: " + isTransactionStatus()
                + "\n");
    }
}
