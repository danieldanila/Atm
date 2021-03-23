package com.atm.service;

import com.atm.dto.CreateBankAccount;
import com.atm.model.BankAccount;
import com.atm.model.Deposit;
import com.atm.model.Transaction;
import com.atm.repository.AtmRepository;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AtmPreLoginServiceImpl implements AtmPreLoginService {

    Scanner scanner = new Scanner(System.in);
    AtmRepository atmRepository;

    public AtmPreLoginServiceImpl(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    public void accountCreate() {
        System.out.print("\nPlease enter your name in order to create a bank account: ");
        String accountName = scanner.nextLine();

        byte numberAttempts = 2;
        while (!accountName.matches("^[ A-Za-z]+$") && numberAttempts > 0) {
            numberAttempts--;
            System.out.print("A name can contain only letters, please try again.\nEnter your name: ");
            accountName = scanner.nextLine();
        }

        if (accountName.matches("^[ A-Za-z]+$")) {
            System.out.print("Hello " + accountName + "! Thank you for choosing Danila Bank!\nPlease enter a PIN for your bank account: ");
            String accountPin = scanner.nextLine();

            numberAttempts = 2;
            while (!(accountPin.length() == 4 && accountPin.chars().allMatch(Character::isDigit)) && numberAttempts > 0) {
                numberAttempts--;
                System.out.print("The PIN you entered is invalid. A PIN must have 4 digits.\nPlease enter a PIN for your bank account: ");
                accountPin = scanner.nextLine();
            }

            if (accountPin.length() == 4 && accountPin.chars().allMatch(Character::isDigit)) {
                System.out.println("The PIN you entered is valid. Creating account...");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Oops, something went wrong!\n");
                }

                CreateBankAccount newAccount = new CreateBankAccount();
                BankAccount bankAccount = new BankAccount(accountName, accountPin, newAccount.getAccountBalance());

                System.out.println("\nThe bank account with the NAME: \"" + bankAccount.getAccountName() + "\" was successfully created with the unique ID: " + bankAccount.getAccountId() + ", PIN: \"" + bankAccount.getAccountPin() + "\" and the ACCOUNT BALANCE: $" + newAccount.getAccountBalance() + "!\n");
                atmRepository.accountCreate(bankAccount);
            } else {
                System.out.println("\nThe PINs you entered are invalid. Please ask an employee from Danila Bank for help!\n");
            }
        } else {

            System.out.println("\nThe NAMES you entered are invalid. Please ask an employee from Danila Bank for help!\n");
        }
    }

    public BankAccount accountAuthentication(byte numberAttempts) {
        System.out.print("\nWelcome back. Please enter the unique ID of the bank account you wish to access: ");

        try {
            UUID accountId = UUID.fromString(scanner.nextLine());
            BankAccount bankAccount = atmRepository.getBankAccount(accountId);

            if (bankAccount != null) {
                System.out.print("Unique ID found. Please enter the NAME corresponding to the unique ID: ");
                String accountName = scanner.nextLine();

                while (numberAttempts > 0 && !bankAccount.getAccountName().equalsIgnoreCase(accountName)) {
                    numberAttempts--;

                    if (numberAttempts == 1) {
                        System.out.println("The entered name doesn't correspond with the unique ID. Please try again. You have " + numberAttempts + " more attempt");
                    } else if (numberAttempts > 0) {
                        System.out.println("The entered name doesn't correspond with the unique ID. Please try again. You have " + numberAttempts + " more attempts");
                    } else {
                        System.out.println("The entered name doesn't correspond with the unique ID. " + numberAttempts + " attempts left.\n");
                    }
                    if (numberAttempts > 0) {
                        System.out.print("Please enter the account NAME: ");
                        accountName = scanner.nextLine();
                    }
                }

                if (bankAccount.getAccountName().equalsIgnoreCase(accountName)) {
                    return bankAccount;
                }

            } else {
                System.out.println("\nThe unique ID you entered is not associated with any account at Danila Bank. Please try again!\n");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nThe unique ID you entered is invalid. A unique ID has the following format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where 'x' represents a digit or a character.\n");
        }
        return new BankAccount("NULL404", "0000", 0f);
    }

    @Override
    public BankAccount accountLogin() {
        byte numberAttempts = 3;

        BankAccount bankAccount = accountAuthentication(numberAttempts);

        while (numberAttempts > 0 && !bankAccount.getAccountName().equalsIgnoreCase("NULL404")) {
            if (numberAttempts == 3) {
                System.out.print("Welcome back " + bankAccount.getAccountName() + ". Please enter your account PIN: ");
            }
            String accountPin = scanner.nextLine();

            if (bankAccount.getAccountPin().equalsIgnoreCase(accountPin)) {
                System.out.println("You have successfully logged in into your bank account!\n");
                atmRepository.accountLogin();

                return bankAccount;
            } else {
                numberAttempts--;
                if (numberAttempts == 1) {
                    System.out.println("Incorrect PIN, please try again. You have " + numberAttempts + " more attempt.");
                } else if (numberAttempts > 0) {
                    System.out.println("Incorrect PIN, please try again. You have " + numberAttempts + " more attempts.");
                } else {
                    System.out.println("Incorrect PIN. " + numberAttempts + " attempts left.\n");
                }
            }
            if (numberAttempts > 0) {
                System.out.print("Please enter your account PIN: ");
            }
        }
        return new BankAccount("NULL404", "0000", 0f);
    }

    @Override
    public void accountDeposit(BankAccount bankAccount, boolean isAccountLogged) {
        byte numberAttempts = 3;

        if (!isAccountLogged) {
            bankAccount = accountAuthentication(numberAttempts);

            if (!bankAccount.getAccountName().equalsIgnoreCase("NULL404")) {
                System.out.print("The account with the unique ID: \"" + bankAccount.getAccountId() + "\" and the NAME: \"" + bankAccount.getAccountName() + "\" found.\nPlease enter the money amount you wish to DEPOSIT to this bank account: ");
            }
        } else {
            System.out.print("\nPlease enter the money amount you wish to DEPOSIT to your bank account: ");
        }

        if (!bankAccount.getAccountName().equalsIgnoreCase("NULL404")) {
            try {
                double amountDeposit = scanner.nextDouble();

                numberAttempts = 2;
                while (amountDeposit <= 0 && numberAttempts > 0) {
                    numberAttempts--;
                    System.out.print("You entered: $" + amountDeposit + ". Please enter a positive and a non-zero amount of money!\nEnter the money amount: ");
                    amountDeposit = scanner.nextDouble();
                }
                scanner.nextLine();

                if (amountDeposit > 0) {
                    bankAccount.setAccountBalance(bankAccount.getAccountBalance() + amountDeposit);
                    if (!isAccountLogged) {
                        System.out.println("\nThe money deposit was successful. You successfully deposited $" + amountDeposit + " to \"" + bankAccount.getAccountId() + "\" bank account!\n");
                    } else {
                        System.out.printf("\nThe money deposit was successful. The old account BALANCE: $%.2f. The new account BALANCE: $%.2f!\n\n", (bankAccount.getAccountBalance() - amountDeposit), bankAccount.getAccountBalance());
                    }
                    Transaction transactionDeposit = new Deposit((bankAccount.getAccountBalance() - amountDeposit), bankAccount.getAccountBalance(), bankAccount.getAccountBalance() - (bankAccount.getAccountBalance() - amountDeposit), true);
                    atmRepository.accountDeposit(bankAccount, transactionDeposit);
                } else {
                    System.out.println("\nThe MONEY AMOUNTS you entered are invalid. Please ask an employee from Danila Bank for help!\n");
                    Transaction transactionDeposit = new Deposit(bankAccount.getAccountBalance(), bankAccount.getAccountBalance(), 0, false);
                    atmRepository.accountDeposit(bankAccount, transactionDeposit);
                }
            } catch (InputMismatchException e) {
                System.out.println("\nError: You entered a non-numeric value. Please try again!\n");
                scanner.nextLine();
            }
        }
    }
}