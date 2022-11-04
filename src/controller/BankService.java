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
                                                "2. Log out\n" +
                                                "0. Exit\n");
                            int state2 = scanner.nextInt();

                            switch (state2) {
                                case 0:
                                    exit();
                                    break;
                                case 1:
                                    System.out.println("Balance: " + user.getBalance() + "\n");
                                    break;

                                case 2:
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
}

