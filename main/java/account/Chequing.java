package account;

import atm.ATM;

/**
 *Chequing is a child class of {@link Asset} and represents a user's chequing account.
 */
public class Chequing extends Asset {

    /**
     * Represents a boolean that is true if this account is the user's primary chequing account.
     */
    public boolean primaryStatus;

    /**
     * Constructor of Chequing class.
     * Calls on super ({@link Asset} and also initializes an account
     * as non-primary and updates {@link Account#type}.
     * @param accountNum the unique account number
     * @param atm instance of the ATM class
     */
    public Chequing(int accountNum, ATM atm){
        super(accountNum, atm);
        primaryStatus = false;
        this.type = "chequing";
    }

    @Override
    public void setPrimary(){
        primaryStatus = true;
    }

    @Override
    public boolean isPrimary(){
        return primaryStatus;
    }

    /**
     * Removes money from the account and decreases the balance.
     * There is a maximum of $100 in overdraft per chequing account.
     * @param amount dollar amount removed from the account
     */
    public void removeMoney(double amount) {
        if (balance >= 0 && balance - amount >= -100) {
            balance -= amount;
        }
    }
}
