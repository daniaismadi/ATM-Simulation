package interfaces;

import atm.ATM;
import atm.User;

import java.io.*;
import java.util.Scanner;

/***
 * Class for subscription interface which manages all subscriptions for a user.
 */
class SubscriptionInterface implements Serializable {
    /***
     * The ATM that this interface runs on.
     */
    private final ATM atm;
    /***
     * The scanner attribute for user inputs.
     */
    private transient Scanner scanner = new Scanner(System.in);

    /***
     * Constructor for SubscriptionInterface.
     *
     * @param a the ATM that this interface runs on
     */
    public SubscriptionInterface(ATM a){
        this.atm = a;
    }

    /***
     * The subscription menu that the user will see in the interface.
     *
     * @param user the user that is viewing the subscription menu
     */
    public void displaySubscriptionMenu(User user) {

        boolean goBack = false;
        scanner = new Scanner(System.in);
        while (!goBack) {
            printChoices();
            String choice = scanner.next();
            switch(choice) {
                case "1":
                    addSubscription(user);
                    break;
                case "2":
                    removeSubscription(user);
                    break;
                case "3":
                    atm.getSubscriber().showUserSubscriptions(user);
                    break;
                case "4":
                    atm.getSubscriber().showAllSubscriptions();
                    break;
                case "5":
                    goBack = true;
                default:
                    System.out.println("Choice not valid. Please enter an integer from 1 to 5.");
                    break;

            }
            System.out.println("Enter choice: ");
        }
    }


    /***
     * Options for the user to pick from for subscriptions.
     */
    private void printChoices(){

        System.out.println("Select Subscription Option: ");
        System.out.println("1: Add Subscription ");
        System.out.println("2: Remove Subscription ");
        System.out.println("3: View current subscriptions");
        System.out.println("4: View available subscriptions");
        System.out.println("5: Exit");

    }

    /***
     * Allows the user to add a subscription. Asks the user to enter the name of the subscription. If the name of the
     * subscription is not already in the ATM, the user will have to enter the how much the subscription is worth
     * so a user will be able to subscribe to anything.
     *
     * @param user the user that would like to add a subscription
     */
    private void addSubscription(User user){
        System.out.println("Enter name of Subscription.");
        scanner = new Scanner(System.in);
        String name = scanner.next();
        atm.getSubscriber().addSubscription(user, name);
    }

    /***
     * Allows the user to remove a subscription. Asks the user the enter the name of the subscription.
     *
     * @param user the user that would like to remove a subscription
     */
    private void removeSubscription(User user){
        System.out.println("Enter name of Subscription.");
        scanner = new Scanner(System.in);
        String name = scanner.next();
        atm.getSubscriber().removeSubscription(user, name);
    }

    /**
     * Used in serialization to store the SubscriptionInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to serialize the subscription interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("SI writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the subscription interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize the object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("SI readObject Failed!");
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
        System.out.println("SI readObjectNoData, this should never happen!");
        System.exit(-1);
    }



}
