package broker;

import atm.ATM;
import atm.User;
import investments.MutualFund;
import investments.Stock;

import java.io.*;
import java.util.Calendar;

/***
 * Class for a mutual fund broker who deals with buying and selling mutual funds for the bank/ATM.
 *
 */
public class BankMutualFundBroker implements Serializable {
    /***
     * The date that is set in the ATM.
     */
    private final Calendar date;

    /**
     * The ATM that this broker is working in.
     */
    private final ATM atm;

    /***
     * Constructor for BankMutualFundBroker.
     *
     * @param atm the ATM that this broker is working in
     */
    public BankMutualFundBroker(ATM atm){
        this.atm = atm;
        this.date = atm.getDate();
    }

    /***
     * Allows the broker to buy stocks into a specified mutual fund.
     * @param fund the fund that the broker wants to buy stocks into
     * @param symbol the symbol of the stocks
     * @param shares the number of shares that the broker wants to buy
     */
    public void buyStocksFund(MutualFund fund, String symbol, int shares){
        boolean valid = atm.getBroker().checkIfStockIsValid(symbol);
        if (valid){
        boolean found = checkIfStockOwned(fund, symbol, shares);
        if(!found){
            buyStockBank(fund, symbol, shares);
            }
        }else{System.out.println("Not a valid symbol, please try again");}
    }

    /***
     * Updates the number of shares of a certain stock if the bank owns the stock in the mutual fund. Returns
     * true if the broker has successfully bought a stock and false otherwise.
     *
     * @param fund the fund that the broker wants to buy into
     * @param symbol the symbol of the stock that the broker wants to buy
     * @param shares the number of shares the the broker wants to buy
     * @return true if the broker has successfully bought a valid stock in the mutual fund and false otherwise
     */
    private boolean checkIfStockOwned(MutualFund fund, String symbol, int shares){
        for(Stock stock : fund.getStocks()){
            if (stock.getSymbol().equals(symbol)){
                stock.increaseNumShares(shares);
                System.out.println("The bank has bought " + shares + " shares of the stock " + stock.getName()
                        + " into the " + fund.getName() + " fund");
                return true;}
        }return false;
    }

    /***
     * Allows the broker to buy a certain number of shares of a new stock for a mutual fund.
     *
     * @param fund the fund that the broker wants to buy
     * @param symbol the symbol of the stock the broker wants to buy
     * @param shares the number of shares the broker wants to buy
     */
    private void buyStockBank(MutualFund fund, String symbol, int shares){
        String stockName = atm.getBroker().companyNameFromSymbol(symbol);
        Stock bought = new Stock(stockName, symbol,0.0);
        bought.setNumShares(shares);
        bought.updateStock(date);
        fund.getStocks().add(bought);
        System.out.println("The bank has bought " + shares + " shares of the stock " + bought.getName()
                + " into the " + fund.getName() + " fund");
    }

    /***
     * Allows the broker to sell stocks from a specified mutual fund given that the stock exists in the fund.
     *
     * @param fund the fund that the broker wants to sell stocks from
     * @param symbol the symbol of the stock that the broker wants to sell
     * @param shares the number of shares tha the broker wants to sell
     */
    public void sellStocksFund(MutualFund fund, String symbol, int shares) {
        boolean valid = atm.getBroker().checkIfStockIsValid(symbol);
        if (valid) {
            boolean found = sellPossible(fund, symbol, shares);
            if (!found) {System.out.println("This stock does not exists in this fund");}
        }else{
            System.out.println("Not a valid symbol, please try again");}
    }

    /***
     * Returns true if it is possible to sell the stock in the mutual fund and false otherwise.
     *
     * @param fund the fund that the broker wants to sell
     * @param symbol the symbol of the stock the broker wants to sell
     * @param shares the number of shares that the broker wants to sell
     * @return true if it is possible to sell the stock and false otherwise
     */
    private boolean sellPossible(MutualFund fund, String symbol, int shares){
        for (Stock stock : fund.getStocks()) {
            if (stock.getSymbol().equals(symbol)) {
                if (stock.getNumShares() >= shares) {
                    stock.decreaseNumShares(shares);
                    System.out.println("The bank has sold " + shares + " shares of the stock " + stock.getName()
                            + " out of the " + fund.getName() + " fund");
                    return true;
                } else {
                    System.out.println("You do not own enough shares please try again");
                    return true;
                }
            }
        } return false;
    }

    /***
     * Allows the user to buy more shares of the stocks in a mutual fund a user wants to invest in so a user can always
     * invest in a fund.
     *
     * @param fund the fund that the broker wants to refill
     * @param amount the amount to increase the fund buy
     */
    public void refillFunds(MutualFund fund, double amount) {
        double oldValue = fund.getValue();
        double newValue = oldValue + amount;
        calculateRefill(fund, newValue);
        updateShareHolders(fund, oldValue, newValue);
    }

    /***
     * Calculates how many stocks should be bought for each stock in the fund.
     *
     * @param fund the fund that needs to be refilled
     * @param amount the amount of the fund needs to be
     */
    private void calculateRefill(MutualFund fund, double amount){
        double netWorth = fund.getValue();
        int  increase =  (int) (amount / netWorth) + 1;
        for (Stock stock: fund.getStocks()){
            stock.setNumShares(stock.getNumShares() * increase);
        }
    }

    /***
     * Updates the percentage owned in the hash map of investors.
     *
     * @param fund the fund of
     * @param oldValue the old value of the fund
     * @param newValue the new value of the fund
     */
    private void updateShareHolders(MutualFund fund, double oldValue, double newValue){
        double increase = newValue / oldValue;
        for (User shareholder : fund.getInvestors().keySet()){
            double oldPercent = fund.getInvestors().get(shareholder).get(1);
            double newPercent = oldPercent / increase;
            fund.getInvestors().get(shareholder).set(1, newPercent);
            shareholder.getInvestmentPortfolio().getMutualFundPortfolio().get(fund).set(1, newPercent);
        }
    }

    /**
     * Used to serialize the BankMutualFundBroker object.
     *
     * @param oos instance of the ObjectOutputStream class to write the UserMutualFundBroker object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Used to deserialize an BankMutualFundBroker object to store the BankMutualFundBroker's information after the ATM is rebooted.
     *
     * @param ois instance of the ObjectInputStream class used to read the account object
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
