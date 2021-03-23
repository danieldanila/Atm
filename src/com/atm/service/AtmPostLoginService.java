package com.atm.service;

import com.atm.model.BankAccount;

public interface AtmPostLoginService {

    boolean pinChange(BankAccount bankAccount);

    void accountWithdraw(BankAccount bankAccount);

    void accountTransfer(BankAccount bankAccountDonor);

    void accountTransaction(BankAccount bankAccount);

    void accountHistory();
}
