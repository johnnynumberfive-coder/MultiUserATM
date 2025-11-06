package com.multiuser.atm;

public class Customer {
    private int id;
    private String accountNumber;
    private String pin;

    public Customer(int id, String accountNumber, String pin) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }
}

