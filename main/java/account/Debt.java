package account;

import atm.ATM;

/***
 * Debt class is a subclass of the {@link Account#type}
 * and the abstract parent class for: {@link CreditCard},
 * and {@link LOC}.
 *
 */
public abstract class Debt extends Account{

    /**
     * The maximum credit limit available for the user to spend per credit card account.
     */
    private final double creditLimit = 50000;

    /**
     * Debt constructor calls on super {@link Account}.
     * @param accountNum unique account number
     * @param atm instance of the ATM
     */
    Debt(int accountNum, ATM atm) {
        super(accountNum, atm);
    }

    /**
     * Adds money to the account and decreases the balance.
     * @param amount dollar amount added into the account
     */
    public void addMoney(double amount){
        this.balance -= amount;
    }

    /**
     * Removes money from the account if credit available and increases the balance.
     * @param amount dollar amount removed from the account
     */
    public void removeMoney(double amount){
        if (checkFundsSufficient(amount)) {
            this.balance += amount;

        } else {
            System.out.println("Transaction declined. This account has reached the maximum credit limit!");
        }
    }

    @Override
    public boolean checkFundsSufficient(double amount) {
        return (balance + amount) <= creditLimit;
    }
}
