package com.multiuser.atm;

import java.sql.*;
import java.util.Optional;

public class CustomerRepository {

    // Auto-generate an 8-digit account number
    private static String generateAccountNumber() {
        int number = (int)(Math.random() * 90000000) + 10000000;
        return String.valueOf(number);
    }

    // Create a new customer with one checking account
    public static Customer createCustomer(String pin) throws SQLException {
        String accountNumber = generateAccountNumber();
        try (Connection conn = Database.getConnection()) {
            // Insert customer
            String insertCustomer = "INSERT INTO Customers(accountNumber, pin) VALUES(?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertCustomer, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, accountNumber);
                ps.setString(2, pin);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int customerId = rs.getInt(1);

                    // Create default checking account
                    String insertAccount = "INSERT INTO Accounts(customerId, type, balance) VALUES(?, 'CHECKING', 0)";
                    try (PreparedStatement psAcc = conn.prepareStatement(insertAccount)) {
                        psAcc.setInt(1, customerId);
                        psAcc.executeUpdate();
                    }

                    return new Customer(customerId, accountNumber, pin);
                } else {
                    throw new SQLException("Failed to retrieve customer ID.");
                }
            }
        }
    }

    // Find customer by account number + pin
    public static Optional<Customer> findCustomer(String accountNumber, String pin) throws SQLException {
        String query = "SELECT * FROM Customers WHERE accountNumber = ? AND pin = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, accountNumber);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return Optional.of(new Customer(id, accountNumber, pin));
            }
            return Optional.empty();
        }
    }

    // Update a customer's pin
    public static void updatePin(int customerId, String newPin) throws SQLException {
        String update = "UPDATE Customers SET pin = ? WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setString(1, newPin);
            ps.setInt(2, customerId);
            ps.executeUpdate();
        }
    }
}
