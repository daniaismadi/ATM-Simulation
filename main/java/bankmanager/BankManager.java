package bankmanager;
import atm.*;
import account.*;

import java.io.*;

/**
 * The Bank Manager class can fulfill requests submitted through the ATM that the ATM cannot perform by
 * either doing them or delegating them to one of 3 other responsible managers:
 * {@link AccountManager}, {@link TransactionManager}, and {@link UserManager}.
 */
public class BankManager implements Serializable{
    /**
     * Instance of {@link ATM}
     */
    private final ATM atm;

    /**
     * Instance of {@link AccountManager}
     */
    private final AccountManager accountManager;

    /**
     * Instance of {@link TransactionManager}
     */
    private final TransactionManager transactionManager;

    /**
     * Instance of {@link UserManager}
     */
    private final UserManager userManager;

    /**
     * Bank Manager constructor.
     * @param atm instance of {@link ATM}
     */
    public BankManager(ATM atm){
        this.atm = atm;
        this.accountManager = new AccountManager(atm);
        this.transactionManager = new TransactionManager();
        this.userManager = new UserManager(atm);

    }

    //Bank manager will always add 100 new bills when restocking

    /**
     * Restocks the ATM by increasing the number of each type of bill to 100 bills
     * and updates alerts.txt to confirm restock.
     * @param index represents the type of bill in {@link Bills} to restock ($5, $10, $20, $50)
     */
    public void restock(int index){
        atm.getBills().setBills(index, 100);
        try {
            //System.out.println(System.getProperty("user.dir"));
            File file = new File(System.getProperty("user.dir") + "/phase1/src/main/Text Files/alerts.txt");
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write("Alerts addressed");
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file alerts.txt");
        }
    }

    /**
     * Creates a bank account through {@link AccountManager}
     * @param user who the account is being created for
     * @param acct_type type of account to create
     */
    public void createAccount(User user, String acct_type) {
        accountManager.createAccount(user, acct_type);
    }

    /**
     * Creates a joint bank account through {@link AccountManager}
     * @param user1 first owner of the joint account
     * @param user2 second owner fo the joint account
     * @param accountType type of account to create
     */
    public void createJointAccount(User user1, User user2, String accountType) {
        accountManager.createJointAccount(user1, user2, accountType);
    }

    /**
     *  Turns a sole owner account into a joint account by adding another owner through {@link AccountManager}
     * @param user the new owner added to the account
     * @param account the account to add the new owner to
     */
    public void addExistingUserToAccount(User user, Account account) {
        accountManager.addExistingUserToAccount(user, account);
    }

    /***
     * Creates a new user through {@link UserManager}.
     * When a new user is created, they will also open
     * one of each type of account which is done through {@link AccountManager}.
     *
     * @param username the username the user uses to log in
     * @param password the password the user uses to log in
     * @return a new {@link User}
     * @see User
     */
    public User createUser(String username, String password) {
        User user = userManager.createUser(username, password);

        accountManager.createAccount(user, "chequing");
        accountManager.createAccount(user, "creditcard");
        accountManager.createAccount(user, "loc");
        accountManager.createAccount(user, "savings");
        accountManager.createAccount(user, "stock");

        return user;
    }

    /**
     * Undoes the last transaction performed by one of the user's accounts.
     * @param user the user who owns the account
     * @param acct the account the user used to perform the transaction
     */
    public void undoTransaction(User user, Account acct) {
        transactionManager.undoTransaction(user, acct);
    }

    /**
     *Used in serialization to store the Bank Manager object.
     * @param oos instance of the ObjectOutputStream class to write the bank manager object
     * @throws IOException IOException if an IO error occurs
     */
    private void writeObject(ObjectOutputStream oos) throws IOException{
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("BM writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     *
     * Used in serialization to restore the bank manager's information after the ATM is restarted.
     * @param ois instance of the ObjectInputStream class used to read the Bank Manager object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("BM readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException{
        System.out.println("BM readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
