package broker;

import atm.*;
import bankmanager.*;
// import org.json.JSONObject;

import java.io.*;


/***
 * Broker is a class that creates a new Broker for the ATM. A broker will be able to trade stocks and mutual funds
 * and will be able to buy and sell into the mutual funds.
 */
public class Broker implements Serializable {

    /**
     * The ATM  instance being used.
     */
    private final ATM atm;

    /**
     * A stock broker to trade stocks.
     */
    private final StockBroker stockBroker;

    /**
     * A mutual funds broker to manage mutual funds.
     */
    private final MutualFundsBroker mutualFundsBroker;

    /**
     * Used for the JSON library.
     */
    private final String json;

    public Broker(ATM atm, BankManager bm) {
        this.atm = atm;
        this.stockBroker = new StockBroker(atm);
        this.mutualFundsBroker = new MutualFundsBroker(atm, this);
        JSONObject jsonObject = loadJSONFromText();
        json = jsonObject.toString();
        bm.createUser("broker", "password");

    }
    /**
     * Gets a StockBroker instance.
     * @return stockbroker
     */
    public StockBroker getStockBroker() {
        return stockBroker;
    }

    /**
     * Gets a MutualFundsBroker instance.
     * @return mutualFundsBroker
     */
    public MutualFundsBroker getMutualFundsBroker() {
        return mutualFundsBroker;
    }


    /**
     * Loads a JSONObject that contains a mapping of stocks to their names.
     *
     * @return a JSONObject that contains a mapping of stocks to their names
     */
    private JSONObject loadJSONFromText(){
        JSONObject json = null;
        try {
            File file = new File(System.getProperty("user.dir") + "/phase1/src/main/Text Files/stocklist.txt");
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            Reader rd = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            json = new JSONObject(sb.toString());
            return json;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound! Why don't you have stocklist.txt?");
            System.exit(-1);
        } catch (IOException e){
            System.out.println("IOException! This shouldn't happen");
            System.exit(-1);
        }
        return json;
    }
    /**
     * Checks if a given symbol is in the database we are sourcing from.
     *
     * @param symbol unique identifier of a stock
     * @return true if a stock symbol is in the database, false otherwise.
     */
    public boolean checkIfStockIsValid(String symbol){
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.has(symbol);
    }
    /**
     * Returns the company name given a stock symbol.
     *
     * @param symbol the unique identifier of a stock
     * @return the company name of the stock symbol.
     */
    public String companyNameFromSymbol(String symbol){
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getString(symbol);
    }

    /**
     * Used to serialize a Broker object.
     *
     * @param oos instance of the ObjectOutputStream class to write the object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("Broker writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used to deserialize a Broker object after the ATM is rebooted.
     *
     * @param ois instance of the ObjectInputStream class used to read the  object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("Broker readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     *
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("Broker readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}