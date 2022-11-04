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
        int checksum;
        for (int i = 0; i < 9; i++) {
            number.append(randomCardNumber.nextInt(10));
        }
        String s = BIN + number;
        //Luhn algorithm
        char[] charArr = new char[15];
        charArr = s.toCharArray();

        int[] nArr = new int[15];

        for (int i = 0; i < charArr.length; i++) {
            nArr[i] = Character.getNumericValue(charArr[i]);

        }
        int sum = 0;
        for (int i = 0; i < nArr.length; i++) {
            if (i == 0 || i % 2 == 0) {
                int digit = 2 * nArr[i];
                if (digit > 9) {
                    digit -= 9;
                }
                sum += digit;
            } else {
                sum += nArr[i];
            }
        }
        if (sum % 10 == 0) {
            checksum = 0;
        } else {
            checksum = 10 - sum % 10;
        }

        return BIN + number + checksum;
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
    public String toString() {
        return "Account{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, pin);
    }

    public static boolean checkCardNumberWithLuhnAlgorithm(String card) {
        if (card.length() != 16) {
            return false;
        }
        char[] charArray = card.toCharArray();
        int[] nArr = new int[15];
        int sum = 0;
        int checksum = Character.getNumericValue(charArray[15]);
        for (int i = 0; i < 15; i++) {
            nArr[i] = Character.getNumericValue(charArray[i]);
        }
        for (int i = 0; i < nArr.length; i++) {
            if (i == 0 || i % 2 == 0) {
                int num = 2 * nArr[i];
                if (num > 9) {
                    num -= 9;
                }
                sum += num;
            } else {
                sum += nArr[i];
            }
        }
        return (sum + checksum) % 10 == 0;
    }
}
