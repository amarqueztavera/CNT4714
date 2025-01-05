/* 
Name: Andrea Marquez Tavera
    Course: CNT 4714 Fall 2024
    Assignment title:
        Project 2 – Synchronized/Cooperating Threads – A Banking Simulation
    Due Date: September 22, 2024
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Define two accounts with initial balance 0 and account names
        System.out.println("* * *   SIMULATION BEGINS...\n");
        System.out.println("Deposit Agents\t\t\t   Withdrawal Agents\t\t\t\t\t       Balance                                                     Transaction Number");
        System.out.println("----------------\t\t\t   -----------------\t\t\t\t\t------------------------\t\t\t\t\t--------------------------");

        BankAccount account1 = new BankAccount(0, "JA-1");
        BankAccount account2 = new BankAccount(0, "JA-2");

        ExecutorService executor = Executors.newFixedThreadPool(20);

        // Add Depositor Agents with names DT#
        for (int i = 0; i < 5; i++) {
            executor.execute(new DepositorAgent(account1, account2, "DT" + i));
        }

        // Add Withdrawal Agents with names WT#
        for (int i = 0; i < 10; i++) {
            executor.execute(new WithdrawalAgent(account1, account2, "WT" + i));
        }

        // Add Transfer Agents
        for (int i = 0; i < 2; i++) {
            executor.execute(new TransferAgent(account1, account2, "Transfer-" + i));
        }

        // Add Internal Audit Agent
        executor.execute(new InternalAuditAgent(account1, account2));

        // Add Treasury Agent
        executor.execute(new TreasuryAgent(account1, account2));

        // The executor doesn't need to be shutdown since the agents run infinitely
        // executor.shutdown();
    }
}
