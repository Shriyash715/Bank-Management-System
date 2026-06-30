import java.util.ArrayList;

public class Account {
    int accountNumber;
    String name;
    double balance;
    ArrayList<String> transactions = new ArrayList<>();

    public Account(int accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        transactions.add("Account created with balance: " + balance);
    }

    public void displayAccount() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
    }

    public void deposit(double amount) {
        balance = balance + amount;
        transactions.add(amount + " deposited");
        System.out.println(amount + " deposited successfully");
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance = balance - amount;
            transactions.add(amount + " withdrawn");
            System.out.println(amount + " withdrawn successfully");
        } else {
            System.out.println("Insufficient balance");
        }
    }

    public void checkBalance() {
        System.out.println("Current Balance: " + balance);
    }

    public void showTransactions() {
        System.out.println("Transaction History:");
        for (String t : transactions) {
            System.out.println(t);
        }
    }
}