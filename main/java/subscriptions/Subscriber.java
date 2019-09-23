package subscriptions;

import account.Account;
import account.CreditCard;
import atm.ATM;
import atm.User;

import java.io.*;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Class that handles the user's subscribing functions
 */
public class Subscriber implements Serializable {
    /**
     *
     *
     * attributes:
     * ATM atm:
     *
     * Methods:
     *
     * addSubscription: adds a new Subscription of the given name to the user's subscriptions (ArrayList)
     * checkCredit: User can make a Subscription if they have a credit card.
     * createSubscription: creates a new Subscription not currently in the atm Subscription list
     * hasSubscriptionAtm: Checks if the atm has a Subscription in it's list
     * hasSubscriptionUser: Checks if the user has a Subscription in their list
     * updateAllSubscriptions: To be called in the atm upon restart,
     * updates all Subscription from every user once a month.
     * showAllSubscriptions: Prints all the current subscriptions in the atm.
     * showUserSubscriptions: Prints all the subscriptions the user has.
     * removeSubscription: Removes given Subscription from user's list.
     */

    /**
     * Performs functions for the specified atm object.
     */
    private final ATM atm;

    /**
     * Subscriber constructor
     * @param a the ATM machine being used
     */
    public Subscriber(ATM a){
        this.atm = a;
    }


    /**
     * Adds a new subscription to the user subscriptions ArrayList.
     * A subscription can only be added to a user if they have a CreditCard Account.
     * @param user user that adds a subscription.
     * @param name name of the subscription.
     */

    public void addSubscription(User user, String name){
        CreditCard hasCredit = checkCredit(user);

        Subscription s;
        boolean userHas = false;
        if (hasCredit != null){
            s = hasSubscriptionUser(name, user);
            userHas = true;
            if (s == null){
                s = hasSubscriptionAtm(name);
                userHas = false;
            }
            if (userHas){
                System.out.println("User already subscribed to: "+name);
            } else if (s != null){
                user.addSubscription(s);
                hasCredit.removeMoney(s.getCost());
            } else {
                s = createSubscription(name);
                user.addSubscription(s);
                hasCredit.removeMoney(s.getCost());
            }
        } else {
            System.out.println("User must create a credit card before making a Subscription.");
        }
    }

    /**
     * Checks if user has a credit card account.
     *
     * @param user the User whose credit is being checked
     * @return returns a CreditCard object from the user's accounts
     */
    private CreditCard checkCredit(User user){
        for (Account ac: user.getAccounts()){
            if (ac instanceof CreditCard){
                return (CreditCard)ac;

            }
        }
        return null;
    }

    /**
     * Creates a new Subscription.
     * @param name name of the subscription created
     * @return Subscription object that is newly created
     */
    private Subscription createSubscription(String name){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter cost of Subscription");

        double cost = -1.00;

        while (cost == -1.00) {

            try {
                cost = Double.parseDouble(sc.next());
                if (cost <= 0) {
                    cost = -1.00;
                    System.out.println("Enter double greater than 0");
                } else {
                    atm.getSubscriptions().addSubscription(name, cost);
                    return new Subscription(name, cost);
                }

            } catch (Exception e) {
                System.out.println("Enter double greater than 0");
            }
        }

        return new Subscription(name, cost);

    }

    /**
     * Checks if ATM currently offers any subscriptions.
     * @param name Name of subscription to be queried.
     * @return {@link Subscription} matching the name queried
     */
    private Subscription hasSubscriptionAtm(String name){
        for (Subscription sub: atm.getSubscriptions().getListOfSubscriptions()){
            if (sub.getName().equalsIgnoreCase(name)){
                return sub;
            }
        }
        return null;
    }

    /**
     * Checks if user has a certain subscription.
     * @param name name of subscription
     * @param user the User
     * @return Subscription that the user has
     */
    private Subscription hasSubscriptionUser(String name, User user){
        for (Subscription sub: user.getSubscriptions()){
            if (sub.getName().equalsIgnoreCase(name)){
                return sub;
            }
        }
        return null;
    }

    /**
     * Charges users for their subscriptions on the 1st of every month.
     */
    public void updateAllSubscriptions(){
        Calendar date = atm.getDate();
        int day = date.get(Calendar.DAY_OF_MONTH);
        if (day == 1){
            for (User user: this.atm.getListOfUsers()){
                CreditCard userCred = checkCredit(user);
                if (userCred != null){
                    for (Subscription sub: user.getSubscriptions()){
                        double m = userCred.getBalance();
                        userCred.removeMoney(sub.getCost());
                        if (userCred.getBalance() == m){
                            user.removeSubsciption(sub.getName());
                            System.out.println("Not enough funds for Subscription: "+sub.getName());
                        }
                    }
                } else {
                    user.removeAllSubscriptions();
                    System.out.println("No Credit card found, all subscriptions cancelled from user: "+user.getUsername());
                }
            }
        }
    }

    /**
     * Prints all the subscriptions currently offered by the atm.
     */
    public void showAllSubscriptions(){
        for (Subscription sub: atm.getSubscriptions().getListOfSubscriptions()){
            System.out.println("Subscription: "+sub.getName()+" Cost: "+sub.getCost());
        }
    }

    /**
     * Prints all the subscriptions of a user.
     * @param user the User with the subscriptions
     */
    public void showUserSubscriptions(User user){
        for (Subscription sub: user.getSubscriptions()){
            System.out.println("Subscription: "+sub.getName()+" Cost: "+sub.getCost());
        }
    }

    /**
     * Removes a subscription from a User's list of subscriptions.
     * @param user the User unsubscribing
     * @param name name of subscription
     */
    public void removeSubscription(User user, String name){
        user.removeSubsciption(name);
    }

    /**
     * Used to serialize Subscriber object.
     * @param oos instance of the ObjectOutputStream class to write the object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Used to deserialize Susbscriber object after the ATM is rebooted.
     *
     * @param ois instance of the ObjectInputStream class used to read the object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}
