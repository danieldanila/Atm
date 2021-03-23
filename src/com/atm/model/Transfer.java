package com.atm.model;

public class Transfer extends Transaction {
    public Transfer(double accountBalanceBeforeTransaction, double accountBalanceAfterTransaction, double accountBalanceDifference, boolean depositStatus) {
        super(accountBalanceBeforeTransaction, accountBalanceAfterTransaction, accountBalanceDifference, depositStatus);
    }

    @Override
    public void transaction() {
        System.out.println("The account balance before the transfer: " + getAccountBalanceBeforeTransaction()
                + "\nThe account balance after the transfer: " + getAccountBalanceAfterTransaction()
                + "\nThe account balance difference: " + getAccountBalanceDifference()
                + "\nThe transfer status: " + isTransactionStatus()
                + "\n");
    }
}
