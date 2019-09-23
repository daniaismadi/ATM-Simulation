package investments;

import atm.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * A class for a mutual fund object.
 *
 */
public class MutualFund implements Serializable {
    /***
     * The risk level of the mutual fund: 1 is low risk, 2 is medium risk and 3 is a high risk fund
     */
    private final int risk;
    /***
     * The name of the mutual fund
     */
    private final String name;
    /***
     * The stocks in that are in this mutual fund
     */
    private final ArrayList<Stock> stocks;
    /***
     * A hash map of users that have invested into this fund as keys and an array list of doubles in the following
     * format: {amount user invested, percentage of the fund the user owns through this investment}
     */
    private final HashMap<User, ArrayList<Double>> investors = new HashMap<>();

    /***
     * Constructor for MutualFund.
     *
     * @param risk the risk level of the mutual fund
     * @param name the name of the mutual fund
     * @param stocks the stocks that are in this mutual fund
     */
    public MutualFund(int risk, String name, ArrayList<Stock> stocks){
        this.risk = risk;
        this.name = name;
        this.stocks = stocks;
    }

    /***
     * Getter for the value of the mutual fund.
     *
     * @return the value of the mutual fund
     */
    public double getValue(){
        double total = 0.0;
        for (Stock stock : stocks) {
            total += stock.getValue() * stock.getNumShares();
        }return total;
    }

    /***
     * Returns the name of the mutual fund.
     *
     * @return the name of the mutual fund.
     */
    public String getName(){return name;}

    /***
     * Returns an array list of stocks in this mutual fund.
     *
     * @return the array list of stocks in this mutual fund.
     */
    public ArrayList<Stock> getStocks(){
        return stocks;
    }

    /***
     * Returns the hash map of users that have invested into this fund as keys and an array list of doubles in the
     * following format: {amount user invested, percentage of the fund the user owns through this investment}
     *
     * @return the hash map
     */
    public HashMap<User, ArrayList<Double>> getInvestors(){
        return investors;
    }

    /***
     * Adds user and how much they invested into the mutual fund.
     *
     * @param user the user that has invested into this mutual fund
     * @param investment an array list of doubles in the following format: {amount user invested, percentage of the
     *                   fund the user owns through this investment}
     */
    public void setInvestors(User user, ArrayList<Double> investment){
        investors.put(user, investment);
        }

    /**
     * Used in serialization to store the MutualFund object.
     *
     * @param oos instance of the ObjectOutputStream class to write the account interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Used in serialization to restore the mutual fund's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the account object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
    }

    /**
     * Reads an object with no data stored in it.
     *
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
