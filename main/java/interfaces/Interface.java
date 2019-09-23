package interfaces;

import atm.*;
import bankmanager.*;

import java.io.*;
import java.util.Scanner;

/***
 * Main interface class that whoever is running the ATM will see.
 *
 */
public class Interface implements Serializable {
    /***
     * The ATM that this interface runs on.
     */
    private final ATM atm;
    /***
     * The bank manager interface that the bank manager will see.
     */
    private final BankManagerInterface bankManagerInterface;
    /***
     * The user interface that a user will see.
     */
    private final UserInterface userInterface;
    /***
     * The broker interface that the broker will see.
     */
    private final BrokerInterface brokerInterface;
    /***
     * The scanner attribute that is used for inputs.
     */
    private transient Scanner scanner;

    /***
     * Constructor for Interface.
     * @param atm the atm that this interface is running on.
     */
    public Interface(ATM atm) {
        this.atm = atm;
        this.bankManagerInterface = new BankManagerInterface(atm);
        this.userInterface = new UserInterface(atm);
        this.brokerInterface = new BrokerInterface(atm);
        this.scanner = new Scanner(System.in);
    }

    /***
     * The login menu that appears automatically when the ATM is run.
     *
     * @return the username that the user has input, if the user has not input a valid username, this method will
     * return ""
     */
    public String displayLoginMenu() {
        scanner = new Scanner(System.in);
        System.out.println("Welcome. Please login.");
        User loginUser;
        System.out.println("Username: ");
        String usernameAttempt = scanner.next();
        System.out.println("Password: ");
        String passwordAttempt = scanner.next();
        if (usernameAttempt.equals("manager") && passwordAttempt.equals("password")) {
            System.out.println("Login successful. Logging in as bank manager.");
            return "manager";
        } else if (usernameAttempt.equals("broker") && passwordAttempt.equals("password")) {
            System.out.println("Login successful. Logging in as broker.");
            return "broker";
        } else {
            for (User usr : atm.getListOfUsers()) {
                if (usr.getUsername().equals(usernameAttempt) && usr.getPassword().equals(passwordAttempt)) {
                    loginUser = usr;
                    System.out.println("Login successful. Logging into " + loginUser.getUsername());
                    return loginUser.getUsername();

                }
            }
        }

        System.out.println("Login Failed, please try again");
        return "";
    }

    /***
     * The broker will be able to choose whether they want to see the broker interface or the user interface since
     * a broker can also be a user. This is what the method is for.
     *
     */
    public void displayBrokerOrUserChoice() {

        scanner = new Scanner(System.in);

        System.out.println("Would you like to sign in as:");
        System.out.println("1. Broker");
        System.out.println("2. User");
        String option = scanner.next();
        boolean validselection = false;

        while (!validselection) {
            switch (option) {
                case "1": {
                    validselection = true;
                    brokerInterface.displayBrokerMenu();
                    break;
                }
                case "2": {
                    validselection = true;
                    userInterface.displayUserMenu(atm.getUser("broker"));
                    break;
                }
                default: {
                    System.out.println("There is no option " + option + ". Please try again.");
                    break;
                }
            }
        }
    }

    /***
     * Allows the interface to display the user menu.
     * @param user the user that wants to log in to the user menu
     */
    public void displayUserMenu(User user) {
        userInterface.displayUserMenu(user);
    }

    /***
     * Allows the interface to display the bank manager menu
     * @param bankManager the bank manager that wants to log in to the bank manager menu
     */
    public void displayManagerMenu(BankManager bankManager) {
        bankManagerInterface.displayManagerMenu(bankManager);
    }

    /**
     * Used in serialization to store the Interface object.
     *
     * @param oos instance of the ObjectOutputStream class to serialize the interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("Interface writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("Interface readObject Failed!");
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
        System.out.println("Interface readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}
