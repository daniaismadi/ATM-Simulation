package subscriptions;

import java.io.*;
import java.util.ArrayList;

/**
 * Represents an available subscription at the ATM.
 *
 */
public class AvailableSubscriptions implements Serializable {


    /**
     * A list that stores all the current subscriptions.
     */
    private final ArrayList<Subscription> listOfSubscriptions = new ArrayList<>();


    /**
     * AvailableSubscriptions constructor.
     * @see AvailableSubscriptions#basicSubscriptions()
     */
    public AvailableSubscriptions(){
        basicSubscriptions();
    }

    /**
     * Adds some basic subscriptions to {@link AvailableSubscriptions#listOfSubscriptions}
     */
    private void basicSubscriptions(){
        listOfSubscriptions.add(new Subscription("netflix", 12.99));
        listOfSubscriptions.add(new Subscription("spotify", 9.50));
        listOfSubscriptions.add(new Subscription("RogersTv", 35.00));
        listOfSubscriptions.add(new Subscription("FidoMobile", 44.99));
    }

    /**
     *
     * @return list of currently avialable subscriptions.
     */
    public ArrayList<Subscription> getListOfSubscriptions(){
        return listOfSubscriptions;
    }

    /**
     * Makes a new subscription available if not offered already.
     *
     * @param name name of the subscription
     * @param price price to pay to subscribe to this subscription
     */
    public void addSubscription (String name, double price){
        boolean added = false;

        for (Subscription sub: listOfSubscriptions){
            if (name.equals(sub.getName())){
                added = true;
            }
        }

        if (added){
            System.out.println("Already added.");
        } else {
            listOfSubscriptions.add(new Subscription(name, price));
            System.out.println("Added: "+name);
        }
    }

    /**
     * Used to serialize an AvailableSubscription object.
     * @param oos instance of the ObjectOutputStream class to write the object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Used to deserialize an AvailableSubscription object to restore it after the ATM is rebooted.
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
