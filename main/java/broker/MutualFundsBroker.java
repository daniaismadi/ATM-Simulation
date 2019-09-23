package broker;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import account.*;
import atm.ATM;
import atm.User;
import investments.*;

/**
 * Class for a mutual funds broker.
 */
public class MutualFundsBroker implements Serializable {

    private final MutualFund lowRiskFund;
    private final MutualFund mediumRiskFund;
    private final MutualFund highRiskFund;
    private final MutualFundsStocks mutualFundsStocks;
    private final ATM atm;
    private final Calendar date;
    private final Broker broker;
    private final BankMutualFundBroker bankMutualFundBroker;
    private final UserMutualFundBroker userMutualFundBroker;


    public MutualFundsBroker(ATM atm, Broker broker){
        this.atm = atm;
        date = atm.getDate();
        this.mutualFundsStocks = new MutualFundsStocks(atm);
        this.lowRiskFund = new MutualFund(1, "lowRiskFund1", mutualFundsStocks.getLowRiskStocks());
        this.mediumRiskFund = new MutualFund(2, "mediumRiskFund1", mutualFundsStocks.getMediumRiskStocks());
        this.highRiskFund = new MutualFund(3, "highRiskFund1", mutualFundsStocks.getHighRiskStocks());
        this.bankMutualFundBroker = new BankMutualFundBroker(atm);
        this.userMutualFundBroker = new UserMutualFundBroker(atm);
        this.broker = broker;

    }

    /**
     *
     * @return a Mutual Fund with low risk stocks in it.
     */
    public MutualFund getLowRiskFund() {return lowRiskFund;}

    /**
     * @return a Mutual Fund with medium risk stocks in it.
     */
    public MutualFund getMediumRiskFund() {return mediumRiskFund;}

    /**
     *
     * @return a Mutual Fund with high risk stocks in it.
     */
    public MutualFund getHighRiskFund() {return highRiskFund;}

    /**
     * Used to access {@link UserMutualFundBroker#buyMutualFunds(User, MutualFund, double)} by the interface.
     * @param user the user buying into the fund
     * @param mutualFund the mutual fund the user is buying into
     * @param amount dollar amount invested in the mutual fund
     */
    public void buyMutualFunds(User user, MutualFund mutualFund, double amount) {
        userMutualFundBroker.buyMutualFunds(user, mutualFund, amount);
    }

    /**
     * Used to access {@link UserMutualFundBroker#sellMutualFunds(User, MutualFund, double)} from the interface.
     * @param user the user selling their mutual fund shares
     * @param mutualFund the mutual fund the user is selling
     * @param amount dollar amount taken out of the mutual fund investment
     */
    public void sellMutualFunds(User user, MutualFund mutualFund, double amount) {
        userMutualFundBroker.sellMutualFunds(user, mutualFund, amount);
    }

    public String toString(User user) {
        return userMutualFundBroker.toString(user);
    }

    /**
     * Used to access {@link BankMutualFundBroker#buyStocksFund(MutualFund, String, int)} by the interface.
     * @param fundToBuy the mutual funds the bank is interested to add to
     * @param symbol the unique identifier of the stocks to add to the fund
     * @param valueOf number of shares of the stock to buy
     */
    public void buyStocksFund(MutualFund fundToBuy, String symbol, Integer valueOf) {
        bankMutualFundBroker.buyStocksFund(fundToBuy, symbol, valueOf);
    }

    /**
     * Used to access {@link BankMutualFundBroker#sellStocksFund(MutualFund, String, int)} by the interface.
     * @param fundToSell the mutual funds the bank is interested to sell from
     * @param symbol the unique identifier of the stocks to remove from the fund
     * @param valueOf number of shares of the stock to sell
     */
    public void sellStocksFund(MutualFund fundToSell, String symbol, Integer valueOf) {
        bankMutualFundBroker.sellStocksFund(fundToSell, symbol, valueOf);
    }

    /**
     * Updates the price the fund every day upon ATM restart.
     */
    public void updateMutualFunds() {
        for(Stock stock : lowRiskFund.getStocks()){stock.updateStock(date);}
        for(Stock stock : mediumRiskFund.getStocks()){stock.updateStock(date);}
        for(Stock stock : highRiskFund.getStocks()){stock.updateStock(date);}
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
