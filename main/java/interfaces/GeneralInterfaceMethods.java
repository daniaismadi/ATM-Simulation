package interfaces;

import account.*;
import atm.*;

import java.io.*;
import java.util.ArrayList;
import java.util.*;

/***
 * A class that has methods that can be implemented in multiple classes, such as AccountInterface, BankManagerInterface
 * and TransactionInterface. The reason for this class is to get rid of duplicate code in those classes these
 * methods can be reused in all those classes.
 *
 */
public class GeneralInterfaceMethods implements Serializable {
    /***
     * The ATM that this interface runs on.
     */
    private final ATM atm;
    /***
     * The scanner attribute that is used for the user to enter inputs into the function.
     */
    private transient Scanner scanner;

    /***
     * Constructor for GeneralInterfaceMethods.
     *
     * @param atm the atm that this interface runs on
     */
    public GeneralInterfaceMethods(ATM atm) {
        this.atm = atm;
    }

    /***
     * Allows the user to create an account. The user will be able to choose the type of account they want to create.
     *
     * @param user the user that wants to create an account
     */
    public void createAccount(User user) {
        String type = selectTypeOfAccount(false);
        atm.getBM().createAccount(user, type);
    }

    /***
     * Allows the user to select the type of account they want to access, whether it's a chequing, line of credit,
     * savings, stocks or credit card account. Returns the type of account as a string. This method accepts a
     * transferout boolean as a parameter because if the user wants to transfer out, they are not allowed to transfer
     * out from a credit card account and so this will not be displayed as an option for the users.
     *
     * @param transferOut a boolean representing the type of transaction the user viewing this current interface
     *                    wants to do is transfer out or not
     * @return the type of account
     */
    public String selectTypeOfAccount(boolean transferOut) {
        // Allows users to pick the type of account they want to access and returns their type as a string.

        StringBuilder toPrint = new StringBuilder("Select the type of account: \n 1. Chequing \n" +
                " 2. Line of Credit \n 3. Savings");


        if (transferOut) {
            toPrint.append("\n 4. Stocks");
            System.out.println(toPrint);
        } else {
            toPrint.append("\n 4. Stocks \n 5. Credit Card");
            System.out.println(toPrint);
        }

        String type = null;
        boolean validselection = false;
        scanner = new Scanner(System.in);


        while (!validselection) {
            type = scanner.next();

            if (type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4") || (!transferOut && type.equals("5"))) {
                validselection = true;
            } else {
                System.out.println("That is not a valid selection. Please try again.");
            }
        }

        return returnTypeOfAccount(type, transferOut);
    }

    /***
     * Helper method for selectTypeOfAccount(). Returns the type of account specified by selectTypeOfAccount() as a string
     * that matches the the type specified in each account class.
     *
     * @param selection the selection made in selectTypeOfAccount()
     * @param transferOut the same boolean transferOut from selectTypeOfAccount()
     * @return the account type as a string
     */
    private String returnTypeOfAccount(String selection, boolean transferOut) {
        // Helper function for selectTypeOfAccount. The function recognizes the selection the user makes and returns
        // the corresponding account type as a string.

        String toReturn = null;

        if (selection.equals("1")) {
            toReturn = "chequing";
        } else if (selection.equals("2")) {
            toReturn = "loc";
        } else if (selection.equals("3")) {
            toReturn = "savings";
        } else if (selection.equals("4")){
            toReturn = "stock";
        } else if (!transferOut && selection.equals("5")) {
            toReturn = "creditcard";
        }

        return toReturn;
    }

    /***
     * Prints out all the accounts of a certain type that a user has.
     *
     * @param listOfAccounts the list of accounts to be printed
     * @param summary whether this prints a summary or not, if summary is true, this means that the user would like
     *                to view a summary of all their accounts and so will print all details of their accounts such
     *                as the last transaction, previous transaction and date created, otherwise it will just print the
     *                account number and balance
     * @return a StringBuilder with the account number, balance, last transaction and date created of the accounts
     * in listOfAccounts
     */
    private StringBuilder printListOfAccounts(ArrayList<Account> listOfAccounts, boolean summary) {
        // Will return a StringBuilder with the account number, balance, last transaction and date
        // created of the accounts a user has.

        StringBuilder choices = new StringBuilder();

        for (Account i : listOfAccounts) {
            choices.append(i.getAccountNum()).append(", Balance: ").append(i.getBalance());
            if (summary) {
                choices.append(", Last Transaction: ");
                if (i.getLastTransaction() != null) {
                    choices.append(i.getLastTransaction().toString());
                } else {
                    choices.append("No previous transaction.");
                }
                choices.append(", Date Created: ").append(i.getDateCreated().getTime());
            }
            choices.append("\n");
        }

        return choices;
    }

    /***
     * Returns an array list of all accounts of a certain type of account that the user has specified. For example,
     * returns all the chequing accounts a user has.
     *
     * @param user the user that wants its list of accounts
     * @param typeOfAccount the type of account
     * @return array list of all the accounts of a certain type specified by user
     */
    public ArrayList<Account> listOfAccounts(User user, String typeOfAccount) {

        ArrayList<Account> accounts = new ArrayList<>();

        for (Account a : user.getAccounts()) {
            if (a.getType().equals(typeOfAccount)) {
                accounts.add(a);
            }
        }

        return accounts;
    }

    /***
     * Prints out the the the accounts a user has.
     *
     * @param user the user that wants to see their list of accounts
     * @param summary true if the user wants to see a complete summary of their accounts, otherwise the user will just
     *                see the account number and balance
     * @param typeOfAccount the type of account that the user wants to see
     */
    public void printChoices(User user, boolean summary, String typeOfAccount) {
        // Prints out the accounts a user has.

        ArrayList<Account> accounts = listOfAccounts(user, typeOfAccount);

        if (typeOfAccount.equals("creditcard")) {
            typeOfAccount = "credit card";
        }

        StringBuilder choices = new StringBuilder("Your " + typeOfAccount + " accounts: \n");
        choices.append(printListOfAccounts(accounts, summary));


        System.out.println(choices);
    }

    /***
     * Returns the account that the user has selected. Allows the user to enter the account number the user
     * wants to perform a transaction on and return that account.
     *
     * @param action the type of action the user would like to perform, this can be deposit, withdraw, transfer in, etc.
     * @param listOfAccounts the list of accounts that the user can select from
     * @return the account the user has selected
     */
    public Account selectAccount(String action, ArrayList<Account> listOfAccounts) {
        // Allows users to select an account by entering their account number. Returns that account.

        System.out.println("Enter the account number you want to " + action + ": ");
        scanner = new Scanner(System.in);

        String accountNumTo = scanner.next();
        StringBuilder accountNumToB = new StringBuilder(accountNumTo);


        boolean valid = true;
        for(int i = 0; i < accountNumToB.length();i++){
            if(!Character.isDigit(accountNumToB.charAt(i))){valid = false;}}


        if(valid) {
            Account account = null;
            for (Account a : listOfAccounts) {
                if (a.getAccountNum() == Integer.valueOf(accountNumTo)){
                    account = a;
                }
            }

            if (account != null) {
                return account;
            }
        }

        System.out.println("The account number you entered is not valid. Please try again.");
        return selectAccount(action, listOfAccounts);
    }

    /***
     * Allows the user to input an amount that they would like to transfer in/out. This method also checks whether
     * the user has input the valid amount. If not, then the method will ask the user to enter again.
     *
     * @return the amount the user has input
     */
    public double selectAmount() {

        System.out.println("Enter the desired amount you would like to transfer: ");
        scanner = new Scanner(System.in);

        String amount = scanner.next();
        StringBuilder amountB = new StringBuilder(amount);

        // Checks that the amount entered by the user is valid.
        boolean valid = true;
        if (amountB.length() >= 3) {
            for (int i = 0; i < amountB.length() - 3; i++) {
                if (!Character.isDigit(amountB.charAt(i))) {
                    valid = false;
                }
            }

            for (int i = amountB.length() - 2; i < amountB.length(); i++) {
                if (!Character.isDigit(amountB.charAt(i))) {
                    valid = false;
                }
            }

            if ((!(amountB.charAt((amountB.length() - 3)) == '.') && (!Character.isDigit(amountB.charAt(amountB.length() - 3))))) {
                valid = false;}

            if ((amountB.charAt((amountB.length() - 3)) == ',')){valid = false;}

        }else{for (int i = 0; i < amountB.length(); i++) {
            if (!Character.isDigit(amountB.charAt(i))) {
                valid = false;}}}

        if(valid){return Double.valueOf(amount);}

        System.out.println("The amount you entered is not possible, please enter an amount rounded to a whole number or to 2 digits.");
        return selectAmount();
    }

    /***
     * Returns the user of the given username.
     *
     * @param username the username of the user
     * @return the User with the specified username
     */
    public User findUser(String username) {
        for (User user : atm.getListOfUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Used in serialization to store the GeneralInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to write the Generalinterface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("GeneralInterfaceMethods writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the general interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("GeneralInterfaceMethods readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected
     *
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("GeneralInterfaceMethods readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}
