package com.multiuser.atm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    // Get all accounts for a customer
    public static List<Account> getAccountsForCustomer(int customerId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM Accounts WHERE customerId = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getInt("customerId"),
                        rs.getString("type"),
                        rs.getDouble("balance")
                );
                accounts.add(account);
            }
        }
        return accounts;
    }

    // Get a single account by ID
    public static Account getAccountById(int accountId) throws SQLException {
        String query = "SELECT * FROM Accounts WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getInt("customerId"),
                        rs.getString("type"),
                        rs.getDouble("balance")
                );
            }
            return null;
        }
    }

    // Update account balance
    public static void updateBalance(int accountId, double newBalance) throws SQLException {
        String update = "UPDATE Accounts SET balance = ? WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    // Deposit amount into account
    public static void deposit(int accountId, double amount) throws SQLException {
        Account account = getAccountById(accountId);
        if (account != null) {
            double newBalance = account.getBalance() + amount;
            updateBalance(accountId, newBalance);
        }
    }

    // Withdraw amount from account
    public static boolean withdraw(int accountId, double amount) throws SQLException {
        Account account = getAccountById(accountId);
        if (account != null && account.getBalance() >= amount) {
            double newBalance = account.getBalance() - amount;
            updateBalance(accountId, newBalance);
            return true;
        }
        return false; // insufficient funds
    }
}

