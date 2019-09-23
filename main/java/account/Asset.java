package account;

import atm.ATM;

/**
 * Asset is a child of {@link Account} and is an abstract class
 * for all asset accounts: {@link Chequing}, {@link Savings}, and {@link StockAccount}.
 */
public abstract class Asset extends Account{

    /**
     * Asset constructor calls on super {@link Account}.
     * @param accountNum unique account number
     * @param atm instance of the ATM
     */
    Asset(int accountNum, ATM atm) {
        super(accountNum, atm);
    }


    /**
     * Adds money to the account and increases the balance.
     * @param amount dollar amount added into the account
     */
    public void addMoney(double amount){
        balance += amount;
    }
}

