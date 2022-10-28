package model;

import java.util.Objects;
import java.util.Random;

public class Account {
    private String cardNumber;
    private String pin;
    private int balance = 0;


    //constructor
    public Account() {
        cardNumber = generateCardNumber();
        pin = generatePin();
    }

    public Account(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String generateCardNumber () {
        Random randomCardNumber = new Random();
        StringBuilder number = new StringBuilder();
        String BIN = "400000";
        int checksum = 0;
        for (int i = 0; i < 9; i++) {
            number.append(randomCardNumber.nextInt(10));
        }
        return BIN + number +checksum;
    }

    //functions
    public String generatePin () {
        Random random = new Random();
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            pin.append(digit);
        }
        return pin.toString();
    }

    //Getter/Setter

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return cardNumber.equals(account.cardNumber) && pin.equals(account.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, pin);
    }
}
