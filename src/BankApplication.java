import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.sql.ResultSet;

public class BankApplication {

    static String url = "jdbc:mysql://localhost:3306/bankdb";
    static String user = "root";
    static String password = "9665811404@St";

    // Find account in ArrayList
    public static Account findAccount(ArrayList<Account> accounts, int accNo) {
        for (Account acc : accounts) {
            if (acc.accountNumber == accNo) {
                return acc;
            }
        }
        return null;
    }

    // Load accounts from MySQL
    public static void loadAccountsFromDatabase(ArrayList<Account> accounts) {

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            String query = "SELECT * FROM accounts";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int accNo = rs.getInt("account_number");
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");

                Account acc = new Account(accNo, name, balance);

                accounts.add(acc);
            }

            rs.close();
            ps.close();
            con.close();

            System.out.println("Accounts loaded successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Save new account
    public static void saveAccountToDatabase(Account acc) {

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            String query =
                    "INSERT INTO accounts(account_number,name,balance) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, acc.accountNumber);
            ps.setString(2, acc.name);
            ps.setDouble(3, acc.balance);

            ps.executeUpdate();

            ps.close();
            con.close();

            System.out.println("Account saved successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveTransaction(int accNo, String type, double amount) {

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            String query =
                    "INSERT INTO transactions(account_number, type, amount) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, accNo);
            ps.setString(2, type);
            ps.setDouble(3, amount);

            ps.executeUpdate();

            ps.close();
            con.close();

            System.out.println("Transaction saved");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Update balance
    public static void updateBalanceInDatabase(int accNo, double newBalance) {

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            String query =
                    "UPDATE accounts SET balance=? WHERE account_number=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setDouble(1, newBalance);
            ps.setInt(2, accNo);

            int rows = ps.executeUpdate();
            System.out.println("Rows Updated = " + rows);

            ps.close();
            con.close();

            System.out.println("Balance updated successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void showTransactions() {

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            String query = "SELECT * FROM transactions";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== Transaction History =====");

            while (rs.next()) {

                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getInt("account_number") + " | " +
                    rs.getString("type") + " | " +
                    rs.getDouble("amount") + " | " +
                    rs.getTimestamp("transaction_date")
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Account> accounts = new ArrayList<>();

        // Load existing accounts
        loadAccountsFromDatabase(accounts);

        int choice;

        do {

            System.out.println("\n===== Bank Menu =====");
            System.out.println("1. Create Account");
            System.out.println("2. Display Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Check Balance");
            System.out.println("6. Show Transactions");
            System.out.println("7. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.print("Enter Account Number: ");
                    int accNo = sc.nextInt();

                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Opening Balance: ");
                    double balance = sc.nextDouble();

                    Account newAcc =
                            new Account(accNo, name, balance);

                    accounts.add(newAcc);

                    saveAccountToDatabase(newAcc);

                    break;

                case 2:

                    System.out.print("Enter Account Number: ");
                    int displayNo = sc.nextInt();

                    Account displayAcc =
                            findAccount(accounts, displayNo);

                    if (displayAcc != null)
                        displayAcc.displayAccount();
                    else
                        System.out.println("Account not found");

                    break;

                case 3:

                    System.out.print("Enter Account Number: ");
                    int depNo = sc.nextInt();

                    Account depAcc =
                            findAccount(accounts, depNo);

                    if (depAcc != null) {

                        System.out.print("Enter Amount: ");
                        double amt = sc.nextDouble();

                        depAcc.deposit(amt);

                        updateBalanceInDatabase(
                                depAcc.accountNumber,
                                depAcc.balance);

                        saveTransaction(
                                depAcc.accountNumber,
                                "Deposit",
                                amt);

                    } else {
                        System.out.println("Account not found");
                    }

                    break;

                case 4:

                    System.out.print("Enter Account Number: ");
                    int witNo = sc.nextInt();

                    Account witAcc =
                            findAccount(accounts, witNo);

                    if (witAcc != null) {

                        System.out.print("Enter Amount: ");
                        double amt = sc.nextDouble();

                        witAcc.withdraw(amt);

                        updateBalanceInDatabase(
                                witAcc.accountNumber,
                                witAcc.balance);

                        saveTransaction(
                                witAcc.accountNumber,
                                "Withdraw",
                                amt);
                    } else {
                        System.out.println("Account not found");
                    }

                    break;

                case 5:

                    System.out.print("Enter Account Number: ");
                    int balNo = sc.nextInt();

                    Account balAcc =
                            findAccount(accounts, balNo);

                    if (balAcc != null)
                        balAcc.checkBalance();
                    else
                        System.out.println("Account not found");

                    break;

                case 6:

                    showTransactions();
                    break;
                    
                case 7:

                    System.out.println("Thank You");
                    break;
                    
                default:

                    System.out.println("Invalid Choice");
            }

        } while (choice != 7);

        sc.close();
    }
}