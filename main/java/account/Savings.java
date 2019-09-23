package account;
import atm.ATM;

/**
 * A child class of {@link Asset} represents a user's savings account.
 */
public class Savings extends Asset {

    /**
     * The monthly interest rate applied to the balance in the savings account at the end of each month.
     */
    private final double interestRate;

    /**
     * Savings constructor calls on super {@link Asset} and
     * initalizes {@link Savings#interestRate} and sets
     * {@link Account#type} to savings.
     * @param accountNum the unique account number
     * @param atm instance of {@link ATM}
     */
    public Savings(int accountNum, ATM atm){
        super(accountNum, atm);
        interestRate = 1.001;
        this.type = "savings";
    }

    /**
     * Increases {@link Account#balance} according to the {@link Savings#interestRate}.
     */
    public void addInterest(){
        this.balance = (this.balance * interestRate);
    }

    //Removing money from an asset account will decrease its balance

    /**
     * If there is sufficient funds in the savings account,
     * removes the specified amount and decreases the balance.
     * @param amount dollar amount removed from the account
     */
    public void removeMoney(double amount){
        if(checkFundsSufficient(amount)) {
            balance -= amount;
        }else {
            System.out.println("Insufficient funds in Savings! Please try another amount or account.");
        }
    }
}
