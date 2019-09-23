package account;

import atm.ATM;

/**
 * CreditCard is a child class of the Debt class.
 */
public class CreditCard extends Debt {

    /**
     * CreditCard constructor calls on super {@link Debt} and sets {@link Account#type} to credit card.
     * @param accountNum the unique account number
     * @param atm instance of the ATM class
     */
    public CreditCard(int accountNum, ATM atm) {
        super(accountNum, atm);
        this.type = "creditcard";
    }

    public void transferOut(double amount, Account accountTo) {
        System.out.println("You cannot transfer out from a Credit Card account. Please try another account.");
    }

    public void payBill(double amount, String receiver) {
        System.out.println("You cannot pay bills from a Credit Card account. Please try another account.");
    }
}