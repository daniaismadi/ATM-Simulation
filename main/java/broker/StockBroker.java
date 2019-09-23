package broker;

import account.Account;
import account.Asset;
import atm.ATM;
import atm.User;
import investments.InvestmentPortfolio;
import investments.MutualFundsStocks;
import investments.Stock;

import java.io.*;
import java.util.ArrayList;

/**
 * Class to Implement Functions of Broker related to stocks.
 * It has an attribute function of type ATM, so all the methods
 * can be calibrated to ATM.date.
 */
public class StockBroker implements Serializable {

    private final ATM atm;

    /**
     *
     * Stock Broker Constructor
     * @param Atm instance of the ATM being used
     */
    StockBroker(ATM Atm){
        this.atm = Atm;
    }

    /**
     * Buys stocks of given share amount for a user.
     *
     * @param symbol: the symbol in String of the stock the user wants to purchase.
     * @param Iv : The user's investment portfolio.
     * @param sa : The user's stocks account.
     * @param shares : The number of shares to be purchased.
     */
    public void buyStocks(String symbol, int shares, Account sa, InvestmentPortfolio Iv) {

        boolean bought = false;
        boolean contains = buyOwnedStock(symbol, shares, sa, Iv);

        if (shares <= 0){
            System.out.println("Enter Share amount greater than 0");
        } else if (!contains){
            bought = buyNewStock(symbol, shares, sa, Iv);
        }

        if (!bought){
            System.out.println("Stocks not purchase because of insufficient funds or invalid symbol");}
    }

    /**
     * Buys stocks of given share amount for a user (if the user already owns some shares of it)
     *
     * @param symbol: the symbol in String of the stock the user wants to purchase.
     * @param Iv : The user's investment portfolio.
     * @param sa : The user's stocks account.
     * @param shares : The number of shares to be purchased.
     * @return : Returns True if bought.
     */
    private boolean buyOwnedStock(String symbol, int shares, Account sa, InvestmentPortfolio Iv) {
        if (shares > 0) {
            for (Stock st : Iv.getStockPortfolio()) {
                if (st.getSymbol().equalsIgnoreCase(symbol)) {
                    if ((st.getValue() * shares) <= sa.getBalance()) {
                        sa.removeMoney(st.getValue() * shares);
                        st.increaseNumShares(shares);
                        System.out.println("You have bought " + shares + " shares of " + st.getName());
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Helper function to buyStocks(), purchases a stock not already owned by User.
     *
     * @param symbol: the symbol in String of the stock the user wants to purchase.
     * @param shares: The number of shares to be purchased.
     * @param sa: The user's stocks account.
     * @param Iv: The user's investment portfolio.
     * @return : Returns True if bought.
     */
    private boolean buyNewStock(String symbol, int shares, Account sa, InvestmentPortfolio Iv) {
        boolean valid = atm.getBroker().checkIfStockIsValid(symbol);
        if (valid){
        Stock st = fetchStock(symbol);
        if (st.getValue() != 0 && shares > 0){
            if (st.getValue() * shares <= sa.getBalance()){
                Iv.getStockPortfolio().add(st);
                st.setNumShares(shares);
                sa.removeMoney(st.getValue() * shares);
                System.out.println("You have bought " + shares + " shares of " + st.getName());
                return true;
            }
        } else {
            if (shares <= 0){
                System.out.println("Enter share amount greater than 0");
            }
            else {
                System.out.println("There is no stock of symbol: " + symbol);
            }
        }}
        return false;
    }

    /**
     * Returns a stock of the given symbol.
     * @param symbol: Symbol of stock.
     * @return Stock object of given symbol
     */
    private Stock fetchStock(String symbol){
        String stockName = atm.getBroker().companyNameFromSymbol(symbol);
        Stock st = new Stock(stockName, symbol,0);
        st.updateStock(atm.getDate());
        return st;
    }


    /**
     * Sells stocks of given symbol and share amount for user.
     * @param SA: The user's stocks account.
     * @param symbol: Symbol of stock to be sold.
     * @param shares: Number of shares to be sold.
     * @param IV: The user's stock portfolio.
     */
    public void sellStocks(Asset SA, String symbol, int shares, InvestmentPortfolio IV) {
        boolean sold = false;
        for (Stock st: IV.getStockPortfolio()){
            if (st.getSymbol().equalsIgnoreCase(symbol)){
                if (shares <= st.getNumShares()) {
                    st.decreaseNumShares(shares);
                    SA.addMoney(shares * st.getValue());
                    sold = true;
                    System.out.println("You have sold " + shares + " shares of " + st.getName());
                    break;
                }
            }
        }
        if (!sold){
            System.out.println("Not enough shares, or stock is not owned by user. ");
        }
    }


    /**
     * Updates all the stocks in the atm to their current value.
     */
    public void updateAllStocks() {
        for (User user:atm.getListOfUsers()){
            for (Stock st:user.getInvestmentPortfolio().getStockPortfolio()){
                st.updateStock(atm.getDate());
            }
        }
    }

    /**
     * Returns the total money a user owns in stocks.
     * @param user: User that owns stocks.
     * @return Amount of type double, of the user's networth in Stocks.
     */

    public double getTotalStockWorth(User user) {
        double total = 0.0;
        for (Stock st: user.getInvestmentPortfolio().getStockPortfolio()){
            total += st.getValue() * st.getNumShares();
        }
        return total;
    }

    /**
     * Prints all the stocks and shares of a user.
     * @param user: user object.
     */
    public void viewUserStocks(User user) {
        ArrayList<Stock> Iv = user.getInvestmentPortfolio().getStockPortfolio();
        System.out.println("User Currently owns: "+Iv.size()+" types of stocks");
        for (Stock st: Iv){
            System.out.println("Stock: "+st.getSymbol()+"; Shares: "+st.getNumShares());
        }
    }

    /**
     * Prints values of the stocks a user owns.
     * @param user the user who owns the stocks
     * @return a string of all the stocks the user owns and their value
     */
    public String stocksToString(User user){
        String totalStocks = "";
        for (Stock stock : user.getInvestmentPortfolio().getStockPortfolio()){
            totalStocks += stock.toString();
        } return totalStocks + "\nTotal value of all your stocks: " + getTotalStockWorth(user) + "$";
    }

    /**
     * Checks if the user owns shares of a specific stocks.
     *
     * @param user the user we are checking for
     * @param symbol the unique identifier of the stock we are checking for
     * @return boolean that is true if the user owns shares of that stock and false otherwise
     */
    public boolean checkIfUserHasStock(User user, String symbol) {
        for (Stock stock : user.getInvestmentPortfolio().getStockPortfolio()) {
            if (stock.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("StockBroker writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("StockBroker readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("StockBroker readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}

