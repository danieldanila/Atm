package com.atm.controller;

import com.atm.model.BankAccount;
import com.atm.service.AtmPostLoginService;
import com.atm.repository.AtmRepository;
import com.atm.repository.AtmRepositoryImpl;
import com.atm.service.AtmPostLoginServiceImpl;
import com.atm.service.AtmPreLoginService;
import com.atm.service.AtmPreLoginServiceImpl;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class AtmControllerImpl implements AtmController {

    AtmRepository atmRepository;
    AtmPreLoginService atmPreLoginService;
    AtmPostLoginService atmPostLoginService;

    public AtmControllerImpl() {
        atmRepository = new AtmRepositoryImpl();
        atmPreLoginService = new AtmPreLoginServiceImpl(atmRepository);
        atmPostLoginService = new AtmPostLoginServiceImpl(atmRepository);
    }

    @Override
    public void atmStart() {
        final String accountCreateInput = "create";
        final String accountLoginInput = "login";
        final String accountDepositInput = "deposit";
        final String pinChangeInput = "pin";
        final String accountWithdrawInput = "withdraw";
        final String accountTransferInput = "transfer";
        final String accountTransactionInput = "transaction";
        final String accountHistoryInput = "history";
        final String accountLogoutInput = "logout";
        final String atmStopInput = "stop";

        Scanner scanner = new Scanner(System.in);
        BankAccount bankAccount = new BankAccount();
        String userInput = "";
        boolean areAccountsCreated = false;
        boolean isAccountLogged = false;

        while (!userInput.equalsIgnoreCase(atmStopInput)) {
            System.out.print("""
                    Welcome to Danila's ATM!
                    This ATM has the followings commands:
                    \t[create] to create a new account at Danila Bank,
                    \t[login] to log in to an existing account at Danila Bank,
                    \t[deposit] to deposit money to an existing account,
                    \t[stop] to stop the ATM.
                    Please enter the word corresponding to the desired command:\t""");
            userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase(accountCreateInput)) {
                atmPreLoginService.accountCreate();
                areAccountsCreated = true;
            } else if (userInput.equalsIgnoreCase(accountLoginInput) && areAccountsCreated) {
                bankAccount = atmPreLoginService.accountLogin();

                if (!bankAccount.getAccountName().equalsIgnoreCase("NULL404")) {
                    isAccountLogged = true;
                }
            } else if (userInput.equalsIgnoreCase(accountDepositInput) && areAccountsCreated) {
                atmPreLoginService.accountDeposit(bankAccount, isAccountLogged);
            } else if ((userInput.equalsIgnoreCase(accountLoginInput) || userInput.equalsIgnoreCase(accountDepositInput)) && !areAccountsCreated) {
                System.out.println("There are no bank accounts registered at this moment. Please create a new bank account.\n");
            } else if (!userInput.equalsIgnoreCase(atmStopInput)) {
                System.out.println("The command you entered is invalid. Please try again!\n");
            } else {
                System.out.println("The ATM is stopping now. Thank you for your visit!");
            }

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                System.out.println("Oops, something went wrong!\n");
            }

            while (isAccountLogged) {
                System.out.printf("""
                        Welcome %s to Danila's ATM!
                        This ATM has the followings commands:
                        \t[deposit] to deposit money to your account,
                        \t[pin] to change your PIN,
                        \t[withdraw] to withdraw money from your bank account,
                        \t[transfer] to transfer to money to another bank account,
                        \t[transaction] to view all your made transactions,
                        \t[history] to view your last commands typed,
                        \t[logout] to log out from your bank account,
                        Please enter the word corresponding to the desired command:\t""", bankAccount.getAccountName());
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase(accountDepositInput)) {
                    atmPreLoginService.accountDeposit(bankAccount, isAccountLogged);
                } else if (userInput.equalsIgnoreCase(pinChangeInput)) {
                    isAccountLogged = atmPostLoginService.pinChange(bankAccount);
                } else if (userInput.equalsIgnoreCase(accountWithdrawInput)) {
                    atmPostLoginService.accountWithdraw(bankAccount);
                } else if (userInput.equalsIgnoreCase(accountTransferInput)) {
                    atmPostLoginService.accountTransfer(bankAccount);
                } else if (userInput.equalsIgnoreCase(accountTransactionInput)) {
                    atmPostLoginService.accountTransaction(bankAccount);
                } else if (userInput.equalsIgnoreCase(accountHistoryInput)) {
                    atmPostLoginService.accountHistory();
                } else if (userInput.equalsIgnoreCase(accountLogoutInput)) {
                    isAccountLogged = false;
                    System.out.println("Thank you for using our services! Logging out...\nGood bye " + bankAccount.getAccountName() + "!\n");
                } else if (!userInput.equalsIgnoreCase(atmStopInput)) {
                    System.out.println("The command you entered is invalid. Please try again!\n");
                } else {
                    System.out.println("Please log out first before stopping the ATM!\n");
                }

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println("Oops, something went wrong!\n");
                }
            }
        }
    }
}