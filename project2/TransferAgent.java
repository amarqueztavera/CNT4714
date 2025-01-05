/* 
Name: Andrea Marquez Tavera
    Course: CNT 4714 Fall 2024
    Assignment title:
        Project 2 – Synchronized/Cooperating Threads – A Banking Simulation
    Due Date: September 22, 2024
*/

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


public class TransferAgent implements Runnable {
    private BankAccount accountFrom;
    private BankAccount accountTo;
    private String agentName;

    public TransferAgent(BankAccount accountFrom, BankAccount accountTo, String agentName) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.agentName = agentName;
    }

    @Override
    public void run() {
        while (true) {
            // Generate a random integer transfer amount between 1 and 99
            int transferAmount = (int) (Math.random() * 99 + 1);

            if (accountFrom.getBalance() >= transferAmount) {
                accountFrom.withdraw(transferAmount, agentName);
                accountTo.deposit(transferAmount, agentName);
                logTransferToCSV(agentName, transferAmount, accountFrom.getAccountName(), accountTo.getAccountName());
                System.out.println("TRANSFER --> " + agentName + " transferring " + transferAmount 
                    + " from account " + accountFrom.getAccountName() + " to account " + accountTo.getAccountName()
                    + " - - account balance is now " + accountFrom.getBalance() 
                    + ", TRANSFER COMPLETE --> account balance is now " + accountTo.getBalance()
                    + " transaction number: " + accountFrom.getTransactionNumber());
            }

            try {
                // Sleep for a random time
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to log transfer transactions into a CSV file
    private void logTransferToCSV(String agentName, int amount, String fromAccount, String toAccount) {
        try (FileWriter fileWriter = new FileWriter("transactions.csv", true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Append a line with agent name, transfer amount, fromAccount, toAccount, and transaction number
            printWriter.printf("%s,Transfer,%s,%d to %s\n", agentName, fromAccount, amount, toAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
