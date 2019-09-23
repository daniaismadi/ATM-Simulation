package interfaces;

import atm.*;

import java.io.*;
import java.util.*;

/***
 * A class for the what the user will be able see when they log in to ATM.
 */
class UserInterface implements Serializable {
    /***
     * The scanner attribute that the user inputs.
     */
    private transient Scanner scanner = new Scanner(System.in);
    /***
     * The ATM that the interface runs on.
     */
    private final ATM atm;
    /***
     * The account interface that the user interface will be able to access.
     */
    private final AccountInterface accountInterface;
    /***
     * The transaction interface that the user interface will be able to access.
     */
    private final TransactionInterface transactionInterface;
    /***
     * The investment interface that the user interface will be able to access.
     */
    private final InvestmentInterface investmentInterface;
    /***
     * The update profile interface tha the user interface will be able to access.
     */
    private final UpdateProfileInterface updateProfileInterface;
    /***
     * The subscription interface that the user interface will be able to access.
     */
    private final SubscriptionInterface subscriptionInterface;

    /***
     * The constructor for UserInterface.
     * @param atm the ATM that this user interface runs on
     */
    public UserInterface(ATM atm) {
        this.atm = atm;
        this.accountInterface = new AccountInterface(atm);
        this.transactionInterface = new TransactionInterface(atm);
        this.investmentInterface = new InvestmentInterface(atm);
        this.updateProfileInterface = new UpdateProfileInterface(atm);
        this.subscriptionInterface = new SubscriptionInterface(atm);
    }

    /***
     * The menu that the user will see when they log in to ATM
     *
     * @param user the user that is viewing the menu
     */
    public void displayUserMenu(User user) {
        boolean logout = false;
        scanner = new Scanner(System.in);

        while (!logout) {
            printOptions();
            String option = scanner.next();
            switch (option) {
                case "1":
                    this.transactionInterface.displayTransactionMenu(user);
                    break;
                case "2":
                    this.accountInterface.displayAccountMenu(user);
                    break;
                case "3":
                    this.investmentInterface.displayInvestmentMenu(user);
                    break;
                case "4":
                    this.updateProfileInterface.displayUpdateProfileMenu(user);
                    break;
                case "5":
                    this.subscriptionInterface.displaySubscriptionMenu(user);
                    break;
                case "6":
                    logout = true;
                    break;
                default:
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 5.");
                    break;
            }
        }

    }

    /***
     * The options the user will be able to select in the user menu.
     *
     */
    private void printOptions() {
        System.out.println("Select an option:");
        System.out.println("1. Perform Transactions");
        System.out.println("2. Manage Accounts");
        System.out.println("3. Manage Investments");
        System.out.println("4. Update User Profile");
        System.out.println("5. Manage subscriptions");
        System.out.println("6. Logout");
        System.out.println("Enter a number: ");
    }

    /**
     * Used in serialization to store the UserInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to write the user interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("UserInterface writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the user interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("UserInterface readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("UserInterface readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
