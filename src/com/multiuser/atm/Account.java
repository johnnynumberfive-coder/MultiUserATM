package com.multiuser.atm;

public class Account {
    private int id;
    private int customerId;
    private String type;
    private double balance;

    public Account(int id, int customerId, String type, double balance) {
        this.id = id;
        this.customerId  = customerId;
        this.type = type;
        this.balance = balance;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getType() { return type; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
