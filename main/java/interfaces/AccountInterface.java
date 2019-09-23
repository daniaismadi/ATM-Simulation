package interfaces;

import atm.*;
import account.*;

import java.io.*;
import java.util.*;

/***
 * Class representing the account menu that will be displayed in the interface for users to manage their accounts.
 *
 */
class AccountInterface implements Serializable {

    /***
     * The ATM that this interface is running on.
     */
    private final ATM atm;

    /***
     * GeneralInterfaceMethods represents a class that contains helper methods that AccountInterface uses.
     */
    private final GeneralInterfaceMethods general;
    /***
     * The scanner attribute that is used for the user to enter inputs into the function.
     */
    private transient Scanner scanner;

    /***
     * Constructor for AccountInterface.
     * @param atm the atm that this atm is running on
     */
    public AccountInterface(ATM atm) {
        this.atm = atm;
        this.general = new GeneralInterfaceMethods(atm);
    }

    /***
     * The account menu that the user sees in the interface.
     *
     * @param user the user that is viewing this interface
     */
    public void displayAccountMenu(User user) {
        boolean goBack = false;

        while (!goBack) {
            printOptions();
            scanner = new Scanner(System.in);
            String option = scanner.next();

            switch (option) {
                case "1":
                    general.createAccount(user);
                    break;
                case "2":
                    requestJointAccountCreation(user);
                    break;
                case "3":
                    addUserToExistingAccount(user);
                    break;
                case "4":
                    summary(user);
                    break;
                case "5":
                    goBack = true;
                    break;
                default:
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 5.");
                    break;
            }
        }
    }

    /***
     * The options the user can pick from in the interface.
     *
     */
    private void printOptions() {
        System.out.println("Select an option:");
        System.out.println("1. Request account creation");
        System.out.println("2. Request joint account creation");
        System.out.println("3. Add user to existing account");
        System.out.println("4. View summary of accounts");
        System.out.println("5. Go back to main menu");
        System.out.println("Enter a number: ");
    }

    /***
     * A method to request to create a new joint account with another user to the Bank manager.
     *
     * @param user1 the user that wants to create the joint account
     */
    private void requestJointAccountCreation(User user1) {
        scanner = new Scanner(System.in);
        System.out.println("Enter the username of the user you would like to open an account with: ");
        String username = scanner.next();

        User user2 = general.findUser(username);

        while (user2 == null || user2 == user1) {
            System.out.println("The user name you entered is not valid. Please enter a valid username (Press * to quit): ");
            username = scanner.next();

            user2 = general.findUser(username);
            if (username.equals("*")) {
                break;
            }
        }

        if (!username.equals("*")) {
            String type = general.selectTypeOfAccount(false);
            atm.getBM().createJointAccount(user1, user2, type);
        }
    }

    /***
     * A method to add an existing user to one of the user's existing accounts.
     *
     * @param user1 the user that wants to add another user into one of its accounts
     */
    private void addUserToExistingAccount(User user1) {
        scanner = new Scanner(System.in);

        String type = general.selectTypeOfAccount(false);
        general.printChoices(user1, false, type);

        Account accountToAddUser = general.selectAccount("add user to", general.listOfAccounts(user1, type));
        System.out.println("Enter the username of the user you would to like to add to this account: ");
        String username = scanner.next();

        User user2 = general.findUser(username);

        while (user2 == null || user2 == user1 ) {
            System.out.println("The user name you entered is not valid. Please enter a valid username (Press * to quit): ");
            username = scanner.next();

            user2 = general.findUser(username);
            if (username.equals("*")) {
                break;
            }
        }

        if (!username.equals("*")) {
            atm.getBM().addExistingUserToAccount(user2, accountToAddUser);
        }
    }

    /***
     * A method to print a summary of all the accounts a user has including the net total of all the accounts together.
     * It will print the account number, the balance, the last transaction on that account and the date created.
     *
     * @param user the user that wants to view a summary of their accounts
     */
    private void summary(User user) {
        // Method for users to see a summary of their accounts.

        general.printChoices(user, true, "chequing");
        general.printChoices(user, true, "loc");
        general.printChoices(user, true, "savings");
        general.printChoices(user, true, "creditcard");
        general.printChoices(user, true, "stock");
        System.out.println("Your net total is: " + user.getNetTotal());

    }

    /**
     * Used in serialization to store the AccountInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to write the account interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("AccountInterface writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the account interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize  object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("AccountInterface readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("AccountInterface readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
