package com.multiuser.atm;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class SetupDatabase {

    // Call this at startup to create tables if they don't exist
    public static void run() {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    accountNumber TEXT UNIQUE NOT NULL,
                    pin TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Accounts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customerId INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    balance REAL DEFAULT 0,
                    FOREIGN KEY (customerId) REFERENCES Customers(id)
                );
            """);

            System.out.println("Database setup complete.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database:");
            e.printStackTrace();
        }
    }
}
