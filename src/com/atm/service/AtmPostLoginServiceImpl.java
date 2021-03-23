package com.atm.service;

import com.atm.model.BankAccount;
import com.atm.model.Transaction;
import com.atm.model.Transfer;
import com.atm.model.Withdraw;
import com.atm.repository.AtmRepository;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AtmPostLoginServiceImpl implements AtmPostLoginService {

    Scanner scanner = new Scanner(System.in);
    AtmRepository atmRepository;

    public AtmPostLoginServiceImpl(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    public boolean pinChange(BankAccount bankAccount) {
        byte numberAttempts = 3;

        System.out.print("\nFor security reasons, please re-enter your PIN: ");
        String accountPin = scanner.nextLine();

        while (numberAttempts > 0 && !bankAccount.getAccountPin().equalsIgnoreCase(accountPin)) {
            numberAttempts--;
            if (numberAttempts == 1) {
                System.out.println("Incorrect PIN, please try again. You have " + numberAttempts + " more attempt.");
            } else if (numberAttempts > 0) {
                System.out.println("Incorrect PIN, please try again. You have " + numberAttempts + " more attempts.");
            } else {
                System.out.println("Incorrect PIN. " + numberAttempts + " attempts left.\n");
                System.out.println("You entered the incorrect PIN several times. For security reasons, we are logging you out.\n");
                return false;
            }

            if (numberAttempts > 0) {
                System.out.print("Please re-enter your PIN: ");
                accountPin = scanner.nextLine();
            }
        }

        if (bankAccount.getAccountPin().equalsIgnoreCase(accountPin)) {
            System.out.print("PIN entered correctly. Please enter the new PIN: ");
            String accountPinNew = scanner.nextLine();

            numberAttempts = 2;
            while (numberAttempts > 0 && (!(accountPinNew.length() == 4 && accountPinNew.chars().allMatch(Character::isDigit)) || accountPinNew.equalsIgnoreCase(bankAccount.getAccountPin()))) {
                System.out.print("The PIN you entered is invalid. A PIN must have 4 digits and can't be the same as the old PIN.\nPlease enter the new PIN for your bank account: ");
                accountPinNew = scanner.nextLine();

                numberAttempts--;
            }

            if (accountPinNew.length() == 4 && accountPinNew.chars().allMatch(Character::isDigit) && !accountPinNew.equalsIgnoreCase(bankAccount.getAccountPin())) {
                System.out.println("\nPIN changed successfully from: \"" + bankAccount.getAccountPin() + "\" to: \"" + accountPinNew + "\"!\n");
                bankAccount.setAccountPin(accountPinNew);

                atmRepository.pinChange(bankAccount);
            } else {
                System.out.println("\nThe PINs you entered are invalid. Please ask an employee from Danila Bank for help!\n");
            }
        }
        return true;
    }

    @Override
    public void accountWithdraw(BankAccount bankAccount) {
        System.out.print("\nPlease enter the amount you want to withdraw from your bank account: ");

        try {
            double amountWithdraw = scanner.nextDouble();

            byte numberAttemps = 2;
            while ((amountWithdraw > bankAccount.getAccountBalance() || amountWithdraw <= 0) && numberAttemps > 0) {
                if (amountWithdraw > bankAccount.getAccountBalance()) {
                    System.out.println("You wanted to withdraw $" + amountWithdraw + " but you only have $" + bankAccount.getAccountBalance() + " in your bank account. Please try again.");
                } else if (amountWithdraw <= 0) {
                    System.out.println("You entered: $" + amountWithdraw + ". Please enter a positive and a non-zero amount of money!");
                }
                System.out.print("Please enter the amount you want to withdraw from your bank account: ");
                amountWithdraw = scanner.nextDouble();

                numberAttemps--;
            }
            scanner.nextLine();

            if (amountWithdraw > bankAccount.getAccountBalance() || amountWithdraw <= 0) {
                System.out.println("\nThe WITHDRAW AMOUNTS you entered are invalid. Please ask an employee from Danila Bank for help!\n");
                Transaction transactionWithdraw = new Withdraw(bankAccount.getAccountBalance(), bankAccount.getAccountBalance(), 0, false);
                atmRepository.accountWithdraw(bankAccount, transactionWithdraw);
            } else {
                bankAccount.setAccountBalance(bankAccount.getAccountBalance() - amountWithdraw);

                System.out.printf("\nThe money withdrawal was successful. The old account BALANCE: $%.2f. The new account BALANCE: $%.2f!\n\n", (bankAccount.getAccountBalance() + amountWithdraw), bankAccount.getAccountBalance());

                Transaction transactionWithdraw = new Withdraw((bankAccount.getAccountBalance() + amountWithdraw), bankAccount.getAccountBalance(), (bankAccount.getAccountBalance() + amountWithdraw) - bankAccount.getAccountBalance(), true);
                atmRepository.accountWithdraw(bankAccount, transactionWithdraw);
            }

        } catch (InputMismatchException e) {
            System.out.println("\nError: You entered a non-numeric value. Please try again!\n");
            scanner.nextLine();
        }
    }

    @Override
    public void accountTransfer(BankAccount bankAccountDonor) {
        System.out.print("\nPlease enter the bank account ID to which you want to make a transfer: ");

        try {
            UUID accountId = UUID.fromString(scanner.nextLine());
            BankAccount bankAccountReceiver = atmRepository.getBankAccount(accountId);

            if (bankAccountReceiver != null && !bankAccountReceiver.getAccountId().equals(bankAccountDonor.getAccountId())) {
                System.out.print("Unique ID found. Please enter the NAME corresponding to the unique ID: ");
                String accountReceiverName = scanner.nextLine();

                byte numberAttempts = 3;
                while (numberAttempts > 0 && !bankAccountReceiver.getAccountName().equalsIgnoreCase(accountReceiverName)) {
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
                        accountReceiverName = scanner.nextLine();
                    }
                }

                if (bankAccountReceiver.getAccountName().equalsIgnoreCase(accountReceiverName)) {
                    System.out.print("The account with the unique ID: \"" + bankAccountReceiver.getAccountId() + "\" and the NAME: \"" + bankAccountReceiver.getAccountName() + "\" found.\nPlease enter the money amount you wish to TRANSFER to this bank account: ");

                    try {
                        double amountTransfer = scanner.nextDouble();

                        numberAttempts = 2;
                        while ((amountTransfer > bankAccountDonor.getAccountBalance() || amountTransfer <= 0) && numberAttempts > 0) {
                            if (amountTransfer > bankAccountDonor.getAccountBalance()) {
                                System.out.println("You wanted to transfer $" + amountTransfer + " but you only have $" + bankAccountDonor.getAccountBalance() + " in your bank account. Please try again.");
                            } else if (amountTransfer <= 0) {
                                System.out.println("You entered: $" + amountTransfer + ". Please enter a positive and a non-zero amount of money!");
                            }
                            System.out.print("Please enter the amount you want to TRANSFER from your bank account: ");
                            amountTransfer = scanner.nextDouble();

                            numberAttempts--;
                        }
                        scanner.nextLine();

                        if (amountTransfer > bankAccountDonor.getAccountBalance() || amountTransfer <= 0) {
                            System.out.println("\nThe TRANSFER AMOUNTS you entered are invalid. Please ask an employee from Danila Bank for help!\n");
                            Transaction transactionWithdraw = new Withdraw(bankAccountDonor.getAccountBalance(), bankAccountDonor.getAccountBalance(), 0, false);
                            atmRepository.accountWithdraw(bankAccountDonor, transactionWithdraw);
                        } else {
                            bankAccountDonor.setAccountBalance(bankAccountDonor.getAccountBalance() - amountTransfer);
                            bankAccountReceiver.setAccountBalance(bankAccountReceiver.getAccountBalance() + amountTransfer);

                            System.out.printf("\nThe money transfer was successful. The old account BALANCE: $%.2f. The new account BALANCE: $%.2f!\n\n", (bankAccountDonor.getAccountBalance() + amountTransfer), bankAccountDonor.getAccountBalance());

                            Transaction transactionWithdraw = new Transfer((bankAccountDonor.getAccountBalance() + amountTransfer), bankAccountDonor.getAccountBalance(), (bankAccountDonor.getAccountBalance() + amountTransfer) - bankAccountDonor.getAccountBalance(), true);
                            Transaction transactionDeposit = new Transfer((bankAccountReceiver.getAccountBalance() - amountTransfer), bankAccountReceiver.getAccountBalance(), bankAccountReceiver.getAccountBalance() - (bankAccountReceiver.getAccountBalance() - amountTransfer), true);
                            atmRepository.accountWithdraw(bankAccountDonor, transactionWithdraw);
                            atmRepository.accountDeposit(bankAccountReceiver,transactionDeposit);
                            atmRepository.accountTransfer();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("\nError: You entered a non-numeric value. Please try again!\n");
                        scanner.nextLine();
                    }
                }

            } else {
                if (bankAccountReceiver != null && bankAccountReceiver.getAccountId().equals(bankAccountDonor.getAccountId())) {
                    System.out.println("\nYou can't make a transfer to the same bank account. Try to deposit money instead!\n");
                } else {
                    System.out.println("\nThe unique ID you entered is not associated with any account at Danila Bank. Please try again!\n");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nThe unique ID you entered is invalid. A unique ID has the following format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where 'x' represents a digit or a character.\n");
        }
    }

    @Override
    public void accountTransaction(BankAccount bankAccount) {
        List<Transaction> transactions = atmRepository.accountTransaction(bankAccount);

        try {
            if (transactions.size() > 0) {
                long transactionNumber = 1;
                for (Transaction transaction : transactions) {
                    System.out.println("\nTransaction number: " + transactionNumber);
                    transaction.transaction();
                    transactionNumber++;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("\nThe bank account did not make any transactions.\n");
        }
    }

    @Override
    public void accountHistory() {
        List<String> userCommandsHistory = atmRepository.accountHistory();

        System.out.print("\nYour commands history from the current session are:{ ");
        for (String userCommand : userCommandsHistory) {
            System.out.print(userCommand + " ");
        }
        System.out.print("}\n\n");
    }
}