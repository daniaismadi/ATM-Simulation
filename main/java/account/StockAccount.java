package account;

import atm.ATM;

/**
 * This class represents a stock account used for investment activities.
 */
public class StockAccount extends Asset {

    /**
     * Stock account constructor.
     * @param accountNum unique account number
     * @param atm instance of the ATM being used
     */
    public StockAccount(int accountNum, ATM atm) {
        super(accountNum, atm);
        this.type = "stock";
    }

    /**
     * Decreases the balance of the stock account by a specified amount
     * @param amount dollar amount removed from the account
     */
    public void removeMoney(double amount) {
        if (balance - amount >= 0) {
            balance -= amount;
        }
    }

    public void payBill(double amount, String receiver) {
        System.out.println("\nYou cannot pay bills from a Stock account. Please try again.");
    }
}
