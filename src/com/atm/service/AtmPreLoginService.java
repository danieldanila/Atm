package com.atm.service;

import com.atm.model.BankAccount;

public interface AtmPreLoginService {

    void accountCreate();

    BankAccount accountLogin();

    void accountDeposit(BankAccount bankAccount, boolean isAccountLogged);
}
