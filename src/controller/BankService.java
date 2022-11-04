package controller;


import model.Account;
import service.DbService;

import java.util.ArrayList;
import java.util.Scanner;

public class BankService {
 //get data from args, check for extension
    public void main(String[] args){
        String fileName = getDbName(args);
        if (fileName == null){
            return;
        }
// create new db

        DbService.createNewDatabase(fileName);
        DbService.createNewTableCard();



        ArrayList <Account> base = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println( "1. Create an account\n" +
                                "2. Log into account \n" +
                                "0. Exit \n");

            int state = scanner.nextInt();
//            if (read != null) {
//                try {
//                    state = Integer.parseInt(read);
//                } catch (NumberFormatException e) {
//                    System.out.println("Print a number");
//                    continue;
//                }
//            } else {
//                break;
//            }

            switch (state) {
                case 0:
                    exit();
                    break;
                case 1:
                    Account newUser = new Account();
                    DbService.saveAccount(newUser);
                    //base.add(newUser);
                    System.out.printf("\nYour card has been created\n" +
                            "Your card number:\n%s\n" +
                            "Your card PIN:\n%s\n\n", newUser.getCardNumber(),
                            newUser.getPin());
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    String number = scanner.next();
                    System.out.println("Enter your PIN:");
                    String pin = scanner.next();
                    Account user = DbService.getAccount(number, pin);
                    if (user != null) {
                        System.out.println("You have successfully logged in!\n");
                        boolean submenu = true;
                        while (submenu) {

                            System.out.println( "1. Balance\n" +
                                                "2. Add income\n" +
                                                "3. Do transfer\n" +
                                                "4. Close account\n" +
                                                "5. Log out\n" +
                                                "0. Exit\n");
                            int state2 = scanner.nextInt();

                            switch (state2) {
                                case 0:
                                    exit();
                                    break;
                                case 1:
                                    user = DbService.getAccount(number, pin);
                                    System.out.println("Balance: " + user.getBalance() + "\n");
                                    break;

                                case 2:
                                    System.out.println("Enter income:");
                                    String income = scanner.next();
                                    addIncome(user.getCardNumber(), income);
                                    break;

                                case 3:
                                    System.out.println("Transfer\nEnter card number:");
                                    String cardNumber = scanner.next();
                                    if (cardNumber == null) break;
                                    if (user.getCardNumber().equals(cardNumber)) {
                                        System.out.println("You can't transfer money to the same account!\n");
                                        break;
                                    }
                                    if (!Account.checkCardNumberWithLuhnAlgorithm(cardNumber)) {
                                        System.out.println("Probably you made mistake in the card number. Please try again!\n");
                                        break;
                                    }
                                    if (!DbService.checkUser(cardNumber)) {
                                        System.out.println("Such a card does not exist.\n");
                                        break;
                                    }

                                    System.out.println("Enter how much money you want to transfer:");
                                    String input = scanner.next();
                                    int money;
                                    if (input != null) {
                                        try {
                                            money = Integer.parseInt(input);
                                        } catch (NumberFormatException e) {
                                            System.out.println("Print a number");
                                            break;
                                        }
                                    } else break;
                                    if (money > DbService.getAccount(user.getCardNumber(), user.getPin()).getBalance()) {
                                        System.out.println("Not enough money!\n");
                                        break;
                                    }
                                    if (money < 0) {
                                        System.out.println("Print positive value\n");
                                        break;
                                    }
                                    DbService.transfer(cardNumber, money);
                                    DbService.subIncome(user.getCardNumber(), money);
                                    System.out.println("Success!\n");
                                    break;

                                case 4:
                                    DbService.closeAccount(user.getCardNumber());
                                    System.out.println("The account has been closed!\n");
                                    submenu =false;
                                    break;

                                case 5:
                                    System.out.println("You have successfully logged out!");
                                    submenu =false;
                                    break;
                            }
                        }

                    } else {
                        System.out.println("Wrong card number or PIN!\n");
                    }
                    break;
            }


        }
    }
    public void exit(){
        DbService.closeDbConnection();
        System.out.println("Bye!");
        System.exit(0);
    }

    private String getDbName(String[] args) {
        String DbName = null;
        if (!args[0].toLowerCase().equals("-filename")) {
            System.out.println("Wrong command");
            return DbName;
        }
        if (!args[1].matches("\\w+\\.s3db")) {      //w+ alphaliteral one or more times (RegEx)
            System.out.println("Wrong file name extension");
            return DbName;
        }
        return args[1];
    }

    private void addIncome(String cardNumber, String input) {

        int income;
        if (input != null) {
            try {
                income = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Print a number");
                return;
            }
        } else {
            return;
        }
        boolean result = false;
        if (income >= 0) {
            result = DbService.addIncome(cardNumber, income);
        } else {
            System.out.println("Income should be a positive value!\n");
            return;
        }
        if (result) {
            System.out.println("Income was added!\n");
        } else {
            System.out.println("Error adding income!\n");
        }
    }


}

