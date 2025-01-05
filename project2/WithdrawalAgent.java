/* 
Name: Andrea Marquez Tavera
    Course: CNT 4714 Fall 2024
    Assignment title:
        Project 2 – Synchronized/Cooperating Threads – A Banking Simulation
    Due Date: September 22, 2024
*/

public class WithdrawalAgent implements Runnable {
    private BankAccount account1;
    private BankAccount account2;
    private String agentName;

    public WithdrawalAgent(BankAccount account1, BankAccount account2, String agentName) {
        this.account1 = account1;
        this.account2 = account2;
        this.agentName = agentName;
    }

    @Override
    public void run() {
        while (true) {
            // Randomly pick between account1 and account2
            BankAccount selectedAccount = Math.random() < 0.5 ? account1 : account2;
            
            // Generate a random integer withdrawal amount between 1 and 99
            int withdrawalAmount = (int) (Math.random() * 99 + 1);
            selectedAccount.withdraw(withdrawalAmount, agentName);
            
            try {
                Thread.sleep((int) (Math.random() * 500)); // Sleep for a random time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
