package com.atm.repository;

import com.atm.model.BankAccount;
import com.atm.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface AtmRepository {

    void accountCreate(BankAccount newAccount);

    void accountLogin();

    void accountDeposit(BankAccount bankAccount, Transaction transactionDeposit);

    void pinChange(BankAccount bankAccount);

    void accountWithdraw(BankAccount bankAccount, Transaction transactionWithdraw);

    void accountTransfer();

    List<Transaction> accountTransaction(BankAccount bankAccount);

    List<String> accountHistory();

    BankAccount getBankAccount(UUID accountId);
}
