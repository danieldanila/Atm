package com.atm.model;

public class Withdraw extends Transaction {

    public Withdraw(double amountBeforeTransaction, double amountAfterTransaction, double amountDifference, boolean withdrawStatus) {
        super(amountBeforeTransaction, amountAfterTransaction, amountDifference, withdrawStatus);
    }

    @Override
    public void transaction() {
        System.out.println("The account balance before the withdrawal: " + getAccountBalanceBeforeTransaction()
                + "\nThe account balance after the withdrawal: " + getAccountBalanceAfterTransaction()
                + "\nThe account balance difference: " + getAccountBalanceDifference()
                + "\nThe withdrawal status: " + isTransactionStatus()
                + "\n");
    }
}
