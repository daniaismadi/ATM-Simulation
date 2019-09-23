package account;

import atm.ATM;

/**
 * A child class of {@link Debt} represents a user's line of credit account.
 */
public class LOC extends Debt{

    /**
     * LOC constructor calls on super {@link Debt} and sets {@link Account#type} to loc.
     * @param accountNum the unique account number
     * @param atm instance of {@link ATM}
     */
    public LOC(int accountNum, ATM atm) {
        super(accountNum, atm);
        this.type = "loc";
    }
}
