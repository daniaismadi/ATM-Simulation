package atm;

import account.Account;
import investments.InvestmentPortfolio;
import subscriptions.Subscription;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a user of the ATM machine.
 */
public class User implements Serializable {

    /**
     * Unique identifier used to login to the ATM machine.
     */
    private final String username;

    /**
     * User's password used to login to the ATM machine.
     */
    private String pass;

    /**
     * A list of all the accounts a user owns.
     */
    private final ArrayList<Account> accounts;

    /**
     * The user's investment portfolio.
     */
    private final InvestmentPortfolio investmentPortfolio;

    /**
     * A list of the user's subscriptions.
     */
    private ArrayList<Subscription> subscriptions;

    /**
     * User constructor.
     *
     * @param username a unique identifier for the user
     * @param password the password user will use to login to the ATM
     * @param accounts list of accounts the user owns
     */
    public User(String username, String password, ArrayList<Account> accounts){
        this.username = username;
        this.pass = password;
        this.accounts = accounts;
        this.subscriptions = new ArrayList<>();
        this.investmentPortfolio = new InvestmentPortfolio();
    }

    /**
     * Returns the net total across all of the accounts the user owns (Assets-Debts)
     *
     * @return the net worth of the user
     */
    public double getNetTotal(){
        double sum = 0;
        for (Account acc : accounts){
            if (acc.getType().equals("chequing") || acc.getType().equals("savings") || acc.getType().equals("stock")) {
            sum += acc.getBalance();
            }else{
                sum -= acc.getBalance();
            }
        }
        return sum;
    }

    /**
     *
     * @return user's investment portfolio
     * @see InvestmentPortfolio
     */
    public InvestmentPortfolio getInvestmentPortfolio(){
        return investmentPortfolio;
    }

    /**
     *
     * @return the list of all the accounts the user owns
     */
    public ArrayList<Account> getAccounts(){
        return accounts;
    }

    /**
     * Checks if the user's stock account has sufficient funds.
     *
     * @param amount the sufficient amount needed in the stock account
     * @return a boolean that is false if the stock account's balance
     *          is less than the amount needed and true otherwise
     */
    public boolean enoughStockBalance(double amount){
        for (Account account : accounts){
            if (account.getType().equals("stock")){
                if (account.getBalance() >= amount){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     *
     * @return the user's username
     */
    public  String getUsername(){
        return username;
    }

    /**
     *
     * @return the user's password
     */
    public String getPassword(){
        return this.pass;
    }

    /**
     *
     * @return the list of the user's subscriptions
     */
    public ArrayList<Subscription> getSubscriptions(){
        return this.subscriptions;
    }

    /**
     * Adds a {@link Subscription} to the user's list of subscriptions
     * @param s a subscription the user has subscribed to
     *
     */
    public void addSubscription(Subscription s){
        subscriptions.add(s);
    }

    /**
     * Unsubscribes the user from all their subscriptions.
     */
    public void removeAllSubscriptions(){
        subscriptions = new ArrayList<>();
    }

    /**
     * Unsubscribes the user from a subscription.
     *
     * @param name name of the subscription user wants to unsubscribe from
     */
    public void removeSubsciption(String name){
        Subscription removal = null;
        for (Subscription sub: subscriptions){
            if (sub.getName().equalsIgnoreCase(name)){
                removal = sub;
                break;
            }
        }
        subscriptions.remove(removal);
    }

    /**
     * Changes the user's password.
     * @param s the new password
     */
    public void setPassword(String s){
        this.pass = s;
    }


    /**
     * Used to serialize the User object.
     *
     * @param oos instance of the ObjectOutputStream class to serialize the User object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("User writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used to deserialize User objects after the ATM reboots.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize User object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("User readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails.
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("User readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
