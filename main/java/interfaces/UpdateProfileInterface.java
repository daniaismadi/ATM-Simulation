package interfaces;

import atm.*;
import account.*;
import bankmanager.*;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.util.Scanner;

/***
 * Class for updating profile interface for the user.
 */
class UpdateProfileInterface implements Serializable {
    /**
     * The ATM that the interface runs on.
     */
    private final ATM atm;
    /***
     * The scanner attribute for user inputs
     */
    private transient Scanner scanner = new Scanner(System.in);

    /***
     * Constructor for UpdateProfileInterface.
     *
     * @param atm the ATM that the interface runs on
     */
    public UpdateProfileInterface(ATM atm) {
        this.atm = atm;
    }

    /**
     * The update profile menu that the user sees in the interface.
     *
     * @param user the user that is viewing the update profile menu
     */

    public void displayUpdateProfileMenu(User user) {
        boolean goBack = false;
        scanner = new Scanner(System.in);

        while (!goBack) {
            printOptions();
            String option = scanner.next();

            switch (option) {
                case "1":
                    changePassword(user);
                    break;
                case "2":
                    goBack = true;
                    break;
                default:
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 2.");
                    break;
            }
        }
    }

    /***
     * The options that the user sees in the interface. Will allow the users to change their password or go back
     * to the main menu.
     *
     */
    private void printOptions() {
        System.out.println("Select an option:");
        System.out.println("1. Change password");
        System.out.println("2. Go back to main menu");
        System.out.println("Enter a number: ");
    }

    /***
     * Allows the user to change their password. The user is not allowed to enter a password with a space. If
     * the user enters a password with a space then only
     *
     * @param user the user that would like to change their password
     */
    private void changePassword(User user) {
        // Method for users to change their password.
        scanner = new Scanner(System.in);

        System.out.println("Type in your new password (spaces not allowed):");
        System.out.println("If you type in a password with a space, only the word before the space will be your password");
        String newPassword = scanner.next();

        user.setPassword(newPassword);
        System.out.println("\nPassword change successful");
    }

    /**
     * Used in serialization to store the UpdateProfileInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to write the update profile interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("UPI writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the update profile interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("UPI readObject Failed!");
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
        System.out.println("UPI readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
