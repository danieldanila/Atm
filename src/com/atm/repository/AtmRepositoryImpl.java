package com.atm.repository;

import com.atm.model.BankAccount;
import com.atm.model.Transaction;

import java.util.*;

public class AtmRepositoryImpl implements AtmRepository {

    Map<UUID, BankAccount> bankAccounts = new HashMap<>();
    Map<UUID, List<Transaction>> bankAccountTransactions = new HashMap<>();

    List<String> userCommandsHistory = new ArrayList<>();
    List<Transaction> transactions;

    @Override
    public void accountCreate(BankAccount newAccount) {
        bankAccounts.put(newAccount.getAccountId(), newAccount);
    }

    @Override
    public void accountLogin() {
        userCommandsHistory.clear();

        userCommandsHistory.add("login");
    }

    @Override
    public void accountDeposit(BankAccount bankAccount, Transaction transactionDeposit) {
        bankAccounts.put(bankAccount.getAccountId(), bankAccount);

        if (bankAccountTransactions.containsKey(bankAccount.getAccountId())) {
            bankAccountTransactions.get(bankAccount.getAccountId()).add(transactionDeposit);
        } else {
            transactions = new ArrayList<>();
            transactions.add(transactionDeposit);
            bankAccountTransactions.put(bankAccount.getAccountId(), transactions);
        }

        userCommandsHistory.add("deposit");
    }

    @Override
    public void pinChange(BankAccount bankAccount) {
        bankAccounts.put(bankAccount.getAccountId(), bankAccount);
        userCommandsHistory.add("pin");
    }

    @Override
    public void accountWithdraw(BankAccount bankAccount, Transaction transactionWithdraw) {
        bankAccounts.put(bankAccount.getAccountId(), bankAccount);
        if (bankAccountTransactions.containsKey(bankAccount.getAccountId())) {
            bankAccountTransactions.get(bankAccount.getAccountId()).add(transactionWithdraw);
        } else {
            transactions = new ArrayList<>();
            transactions.add(transactionWithdraw);
            bankAccountTransactions.put(bankAccount.getAccountId(), transactions);
        }
        userCommandsHistory.add("withdraw");
    }

    @Override
    public void accountTransfer() {
        userCommandsHistory.add("transfer");
    }

    @Override
    public List<Transaction> accountTransaction(BankAccount bankAccount) {
        userCommandsHistory.add("transaction");

        return bankAccountTransactions.get(bankAccount.getAccountId());
    }

    @Override
    public List<String> accountHistory() {
        userCommandsHistory.add("history");

        return userCommandsHistory;
    }

    @Override
    public BankAccount getBankAccount(UUID accountId) {
        return bankAccounts.get(accountId);
    }
}