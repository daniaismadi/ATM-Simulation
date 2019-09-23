package bankmanager;

import account.*;
import atm.*;

import java.io.*;

/**
 * Account Manager handles all requests relating to {@link Account} delegated from Bank Manager.
 */
class AccountManager implements Serializable {

    /**
     * Instance of {@link ATM}
     */
    private final ATM atm;

    /**
     * Keeps track of the last account number assigned to an account to generate unique account numbers.
     */
    private int acct_counter = 1000;

    /**
     * Account Manager constructor.
     *
     * @param atm Instance of {@link ATM}
     */
    public AccountManager(ATM atm){
        this.atm = atm;
    }

    /***
     * Creates an account for user. The type of account is specified by acct_type.
     *
     * @param user the user that wants to create a new account
     * @param acct_type the type of account that user wants to create
     */
    public void createAccount(User user, String acct_type){
        // Creates a new account as specified by the parameter.
        if (acct_type.equalsIgnoreCase("chequing")) {
            createChequingAccount(user, atm);
        }
        else if (acct_type.equalsIgnoreCase("creditcard")) {
            createCreditCard(user, atm);
        }
        else if (acct_type.equalsIgnoreCase("loc")){
            createLOC(user, atm);
        }
        else if (acct_type.equalsIgnoreCase("savings")){
            createSavingsAccount(user, atm);
        } else if (acct_type.equalsIgnoreCase("stock")) {
            createStockAccount(user, atm);
        }
        setPrimaryAccount(user);
    }

    /**
     * Specifies the primary account for a user to which all deposits will automatically get added to.
     *
     * @param user the user that owns the primary account.
     * @see Account#setPrimary()
     */
    private void setPrimaryAccount(User user) {
        boolean primary = false;
        for (Account a : user.getAccounts()) {
            if(a.isPrimary()) {
                primary = true;
            }
        }if(!primary){
            for (Account a : user.getAccounts()) {
                if (a.getType().equalsIgnoreCase("chequing")){
                    a.setPrimary();
                    break;}
            }
        }
    }

    /**
     * Creates a new chequing account.
     *
     * @param user the user that wants to create a new chequing account
     * @param atm the ATM that user uses to create the new chequing account
     */
    private void createChequingAccount(User user, ATM atm){
        user.getAccounts().add(new Chequing(acct_counter, atm));
        System.out.println("New chequing account created.");
        acct_counter+=1;
    }

    /***
     * Creates a new savings account for user.
     *
     * @param user the user that wants to create the new savings account
     * @param atm the ATM that user uses to create the new savings account
     */
    private void createSavingsAccount(User user, ATM atm) {
        user.getAccounts().add(new Savings(acct_counter, atm));
        System.out.println("New savings account created.");
        acct_counter+=1;
    }

    /***
     * Creates a new credit card account for user.
     *
     * @param user the user that wants to create the new credit card account
     * @param atm the ATM that user uses to create the credit card account
     */
    private void createCreditCard(User user, ATM atm) {
        user.getAccounts().add(new CreditCard(acct_counter, atm));
        System.out.println("New credit card created.");
        acct_counter+=1;
    }

    /***
     * Creates a new line of credit account for user.
     *
     * @param user the user that wants to create the line of credit account
     * @param atm the ATM that user uses to create the line of credit account
     */
    private void createLOC(User user, ATM atm) {
        user.getAccounts().add(new LOC(acct_counter, atm));
        System.out.println("New Line of Credit created.");
        acct_counter+=1;
    }

    /***
     * Creates a new stock account for user.
     *
     * @param user the user that wants to create the stock account
     * @param atm the ATM that user uses to create the stock account
     */
    private void createStockAccount(User user, ATM atm) {
        user.getAccounts().add(new StockAccount(acct_counter, atm));
        System.out.println("New Stock Account created.");
        acct_counter+=1;
    }

    /***
     * Creates a joint account with two users.
     *
     * @param user1 the user that wants to create a joint account
     * @param user2 the other user that wants to create a joint account
     * @param accountType the type of account to be created
     */
    public void createJointAccount(User user1, User user2, String accountType) {
        createAccount(user1, accountType);
        Account account = user1.getAccounts().get(user1.getAccounts().size()-1);
        addExistingUserToAccount(user2, account);
    }

    /***
     * Adds an existing user to an existing account that belongs to another user. Up to two people are allowed to share
     * one account.
     *
     * @param user the user that is to be added to the account
     * @param account the account that is to be joined
     */
    public void addExistingUserToAccount(User user, Account account) {
        if (account.getIsJoint()) {
            System.out.println("Not possible to add a user this account because there are already two users sharing" +
                    "this account!");
        } else {
            user.getAccounts().add(account);
            account.setIsJoint(true);
        }
    }

    /**
     * Used in serialization to store the Account Manager object.
     *
     * @param oos instance of the ObjectOutputStream class to write the account manager object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("AM writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the account mananger's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the account object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("AM readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("AM readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}
