/* 
Name: Andrea Marquez Tavera
    Course: CNT 4714 Fall 2024
    Assignment title:
        Project 2 – Synchronized/Cooperating Threads – A Banking Simulation
    Due Date: September 22, 2024
*/

public class TreasuryAgent implements Runnable {
    private BankAccount account1;
    private BankAccount account2;

    public TreasuryAgent(BankAccount account1, BankAccount account2) {
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("***** UNITED STATES DEPARTMENT OF THE TREASURY - BANK AUDIT BEGINNING...");
            System.out.println("The total number of transactions since the last treasury department audit is: " + account1.getTransactionNumber());
            System.out.println("TREASURY DEPT AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR " + account1.getAccountName() + " TO BE " + account1.getBalance());
            System.out.println("TREASURY DEPT AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR " + account2.getAccountName() + " TO BE " + account2.getBalance());
            System.out.println("united states department of the treasury - Bank audit terminated*******");
            try {
                Thread.sleep((int) (Math.random() * 10000)); // Sleep for a random time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
