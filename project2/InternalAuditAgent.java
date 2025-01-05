/* 
Name: Andrea Marquez Tavera
    Course: CNT 4714 Fall 2024
    Assignment title:
        Project 2 – Synchronized/Cooperating Threads – A Banking Simulation
    Due Date: September 22, 2024
*/

public class InternalAuditAgent implements Runnable {
    private BankAccount account1;
    private BankAccount account2;

    public InternalAuditAgent(BankAccount account1, BankAccount account2) {
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("******* Internal bank audit beginning >>> The total number of transactions since the last audit is: " + account1.getTransactionNumber());
            System.out.println("INTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR " + account1.getAccountName() + " TO BE " + account1.getBalance());
            System.out.println("INTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR " + account2.getAccountName() + " TO BE " + account2.getBalance());
            System.out.println("Internal bank audit complete ******");
            try {
                Thread.sleep((int) (Math.random() * 5000)); // Sleep for a random time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
