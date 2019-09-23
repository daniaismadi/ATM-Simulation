package investments;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class containing the stock porfolio and the mutual funds portfolio for a certain user. The stock porfolio will
 * hold all the stocks a user has invested in while the mutual funds portfolio will hold all the mutual funds
 * a user has invested in.
 *
 */
public class InvestmentPortfolio implements Serializable {

    /** An array list of stocks the user has invested in. */
    private final ArrayList<Stock> stockPortfolio = new ArrayList<>();

    /** A hash map that stores the mutual fund the user has invested in as a key and stores an array list containing
     * two values: [amount user invested in the mutual fund, the percentage of the fund the user owns through this
     * investment]. */
    private final HashMap<MutualFund, ArrayList<Double>> mutualFundsPortfolio = new HashMap<>();
    // maps name of the fund: [amount user invested, %owned of the fund through this investment]

    /***
     * Returns the stock portfolio of the user.
     *
     * @return the stock portfolio
     */
    public ArrayList<Stock> getStockPortfolio(){
        return stockPortfolio;
    }

    /***
     * Adds a mutual fund to the mutual funds portfolio to signal that the user has invested into this fund.
     *
     * @param fund the mutual fund the user has invested in
     * @param value an array list containing the amount the user invested in the mutual fund and the percentage of the
     *              the user owns through this investment, in this order
     */
    public void setMutualFundsPortfolio(MutualFund fund, ArrayList<Double> value){
        mutualFundsPortfolio.put(fund, value);
    }

    /***
     * Returns the mutual funds porfolio of the user.
     *
     * @return the hash map of mutual funds the user has invested in as the key and an array list containing two values:
     * [amount user invested in the mutual fund, the percentage of the fund the user owns through this investment]
     */
    public HashMap<MutualFund, ArrayList<Double>> getMutualFundPortfolio(){
        return mutualFundsPortfolio;
    }

    /**
     * Used in serialization to store the InvestmentPortfolio object.
     *
     * @param oos instance of the ObjectOutputStream class to write the account interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Used in serialization to restore the investment portfolio's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the account object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
    }

    /**
     * Used in serialization when class inheritance is not as expected
     *
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
