package subscriptions;

import java.io.*;

/**
 * Represents a subscription that users can subscribe to through the ATM.
 */
public class Subscription implements Serializable {
    /**
     * cost of the subscription.
     */
    private final double cost;

    /**
     * Name of the subscription.
     */
    private final String name;

    /**
     * Subscription constructor.
     * @param n name of the subcription
     * @param c cost of the subscription
     */
    public Subscription(String n, double c){
        this.name = n;
        this.cost = c;
    }

    /**
     *
     * @return name of the subcription
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @return cost of the subcription
     */
    public double getCost(){
        return this.cost;
    }


    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
    }


    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
