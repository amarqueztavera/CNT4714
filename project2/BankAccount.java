/* 
Name: Andrea Marquez Tavera
    Course: CNT 4714 Fall 2024
    Assignment title:
        Project 2 – Synchronized/Cooperating Threads – A Banking Simulation
    Due Date: September 22, 2024
*/
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;


public class BankAccount {
    private double balance;
    private int transactionNumber = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private String accountName;

    // Constructor
    public BankAccount(double initialBalance, String accountName) {
        this.balance = initialBalance;
        this.accountName = accountName;
    }

    // Deposit method
    public void deposit(int amount, String agentName) {
        lock.lock();
        try {
            balance += amount;
            transactionNumber++;
            System.out.println(agentName + " deposits " + amount + " into account " + accountName + " (+) account balance is now " + balance + " transaction number: " + transactionNumber);
            logTransactionToCSV(agentName, "Deposit", amount);
        } finally {
            lock.unlock();
        }
    }

    // Withdraw method
    public void withdraw(int amount, String agentName) {
        lock.lock();
        try {
            if (balance < amount) {
                System.out.println(agentName + " attempts to withdraw " + amount + " from account " + accountName + " (****) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!! Balance only " + balance);
            } else {
                balance -= amount;
                transactionNumber++;
                System.out.println(agentName + " withdraws " + amount + " from account " + accountName + " (-) account balance is now " + balance + " transaction number: " + transactionNumber);
                logTransactionToCSV(agentName, "Withdraw", amount);
            }
        } finally {
            lock.unlock();
        }
    }

    // Method to get the current balance
    public double getBalance() {
        return balance;
    }

    // Method to get the transaction number
    public int getTransactionNumber() {
        return transactionNumber;
    }

    // Method to get the account name
    public String getAccountName() {
        return accountName;
    }

    // Method to log transactions into a CSV file
    private void logTransactionToCSV(String agentName, String transactionType, int amount) {
        try (FileWriter fileWriter = new FileWriter("transactions.csv", true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Append a line with agent name, transaction type, account, amount, and transaction number
            printWriter.printf("%s,%s,%s,%d,%d\n", agentName, transactionType, accountName, amount, transactionNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
