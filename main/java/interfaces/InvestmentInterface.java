package interfaces;

import atm.*;
import account.*;
import investments.*;

import java.io.*;
import java.util.*;

/***
 * Class for the investments interface. Deals with buying and selling stocks and mutual funds.
 *
 */
class InvestmentInterface implements Serializable {
    /***
     * The ATM that the interface is running on.
     */
    private final ATM atm;
    /***
     * The scanner attribute that is used for inputs.
     */
    private transient Scanner scanner = new Scanner(System.in);

    /***
     * Constructor for InvestmentInterface.
     * @param atm the ATM that this interface is running on
     */
    public InvestmentInterface(ATM atm) {
        this.atm = atm;
    }

    /***
     * The investment menu that the user will see in the interface.
     * @param user the user that wants to view the investment menu
     */
    public void displayInvestmentMenu(User user) {
        boolean goBack = false;
        scanner = new Scanner(System.in);

        while(!goBack) {
            printOptions();
            String option = scanner.next();
            switch (option) {
                case "1":
                    buyStocks(user);
                    break;
                case "2":
                    sellStocks(user);
                    break;
                case "3":
                    buyMutualFunds(user);
                    break;
                case "4":
                    sellMutualFunds(user);
                    break;
                case "5":
                    System.out.println(atm.getBroker().getStockBroker().stocksToString(user));
                    break;
                case "6":
                    System.out.println("Your stocks are worth: " +
                            atm.getBroker().getStockBroker().getTotalStockWorth(user) + "$");
                    break;
                case "7":
                    System.out.println(atm.getBroker().getMutualFundsBroker().toString(user));
                    break;
                case "8":
                    goBack = true;
                    break;
                default:
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 8.");
                    break;
            }
        }
    }

    /***
     * The options that the user can pick from in the investments interface.
     *
     */
    private void printOptions() {
        System.out.println("Select an option:");
        System.out.println("1. Buy Stocks");
        System.out.println("2. Sell Stocks");
        System.out.println("3. Buy Mutual Funds");
        System.out.println("4. Sell Mutual Funds");
        System.out.println("5. View your Stocks Investment Portfolio");
        System.out.println("6. View total money in stocks");
        System.out.println("7. View your Mutual Funds Investment Portfolio");
        System.out.println("8. Go Back");
        System.out.println("Enter the number: ");
    }

    /***
     * Allows the user to buy stocks. Will ask the user to input the stock symbol and the number of shares
     * they wish to buy.
     * @param user the user that wants to buy stocks
     */
    private void buyStocks(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Stock symbol: ");
        String symbol = scanner.next();

        while (!atm.getBroker().checkIfStockIsValid(symbol)) {
            System.out.println("Stock symbol is not valid. Please enter again: ");
            symbol = scanner.next();
        }

        System.out.println("Enter number of shares: ");

        int shares;
        try {
            shares = Integer.parseInt(scanner.next());
        } catch (Exception e){
            shares = -1;
        }

        if (shares > 0) {
            atm.getBroker().getStockBroker().buyStocks(symbol, shares, findStockAccount(user), user.getInvestmentPortfolio());
        }
        else {
            System.out.println("Please enter integer greater than 0");
        }
    }

    /***
     * Allows the user to sell stocks. Will ask the user to input the stock symbol and the number of shares
     * they wish to sell.
     * @param user the user that wants to sell stocks
     */
    private void sellStocks(User user) {
        atm.getBroker().getStockBroker().viewUserStocks(user);
        System.out.println("What would you like to sell?");

        System.out.println("Enter Stock symbol: ");
        scanner = new Scanner(System.in);
        String sym = scanner.next();

        // Makes the user re-enter the symbol if they do not have this stock.
        while (!atm.getBroker().getStockBroker().checkIfUserHasStock(user, sym)) {
            System.out.println("You don't own shares of this stock");
            return;
        }

        System.out.println("Enter number of shares: ");
        int shares;
        try {
            shares = Integer.parseInt(scanner.next());
        } catch (Exception e){
            shares = -1;
        }

        if (shares > 0) {
            atm.getBroker().getStockBroker().sellStocks(findStockAccount(user), sym, shares, user.getInvestmentPortfolio());
        }
        else {
            System.out.println("Please enter integer greater than 0.");
        }
    }

    /***
     * Allows the user to buy mutual funds. Will ask the user which fund they would like to invest in and the amount
     * they would like to buy.
     *
     * @param user the user that wants to buy the mutual funds
     */
    private void buyMutualFunds(User user) {
        MutualFund fundToBuy = listFunds();
        System.out.println("Enter the amount you would like to invest: ");
        scanner = new Scanner(System.in);
        String amount = scanner.next();

        atm.getBroker().getMutualFundsBroker().buyMutualFunds(user, fundToBuy, Double.valueOf(amount));
    }

    /***
     * Allows the user to sell mutual funds. Will ask the user which fund they would like to sell and the amount
     * they would like to sell.
     *
     * @param user the user that wants to sell mutual funds
     */
    private void sellMutualFunds(User user) {
        viewUserMutualFunds(user);
        System.out.println("Enter the fund you would like to sell: ");
        scanner = new Scanner(System.in);
        String name = scanner.next();
        MutualFund fundToSell = findMutualFund(user, name);

        while (fundToSell == null) {
            System.out.println("The fund you entered is invalid. Please enter a valid fund name.");
            name = scanner.next();
            fundToSell = findMutualFund(user, name);
        }

        System.out.println("Enter the amount you would like to sell: ");
        String amount = scanner.next();

        atm.getBroker().getMutualFundsBroker().sellMutualFunds(user, fundToSell, Double.valueOf(amount));
    }

    /***
     * Allows the user to select the type of fund. Either a low risk, medium risk or high risk fund.
     * @return The fund the user has selected
     */
    private MutualFund listFunds() {
        System.out.println("Select the type of fund you would like to invest in:");
        System.out.println("1. Low Risk Fund");
        System.out.println("2. Medium Risk Fund");
        System.out.println("3. High Risk Fund");
        System.out.println("Enter the number: ");
        scanner = new Scanner(System.in);
        boolean validSelection = false;

        while (!validSelection) {
            String option = scanner.next();
            switch (option) {
                case "1":
                    return atm.getBroker().getMutualFundsBroker().getLowRiskFund();
                case "2":
                    return atm.getBroker().getMutualFundsBroker().getMediumRiskFund();
                case "3":
                    return atm.getBroker().getMutualFundsBroker().getHighRiskFund();
                default:
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 3.");
                    break;
            }
        }

        return null;
    }

    /***
     * Allows the user to view all the mutual funds they have invested in. They will be allowed to see the fund name,
     * and the amount they have invested in the fund.
     *
     * @param user the user that would like to view their mutual funds
     */
    private void viewUserMutualFunds(User user) {
        HashMap<MutualFund, ArrayList<Double>> mutualFundsPortfolio = user.getInvestmentPortfolio().getMutualFundPortfolio();

        for (Map.Entry<MutualFund, ArrayList<Double>> entry : mutualFundsPortfolio.entrySet()) {
            System.out.println(entry.getKey().getName() + " = " + (entry.getValue().get(1) / 100) * entry.getKey().getValue());
        }

    }

    /***
     * Returns the mutual fund that is in the user's mutual funds portfolio. Returns null if the mutual fund is not
     * in the user's mutual funds portfolio.
     *
     * @param user the user that would like to find the mutual fund
     * @param name the name of the fund that the user wants to find
     * @return the mutual fund that the user wants to find, null if the mutual fund is not found in the user's
     * portfolio
     */
    private MutualFund findMutualFund(User user, String name) {
        HashMap<MutualFund, ArrayList<Double>> mutualFundsPortfolio = user.getInvestmentPortfolio().getMutualFundPortfolio();

        for (Map.Entry<MutualFund, ArrayList<Double>> entry : mutualFundsPortfolio.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /***
     * Returns the user's stock account. Each user will only have one stock account.
     *
     * @param user the user that would like to find their stock account
     * @return the stock account
     */
    private Asset findStockAccount(User user) {
        for (Account account : user.getAccounts()) {
            if (account.getType().equalsIgnoreCase("stock")) {
                return (Asset)account;
            }
        }
        // This will never happen because every user will always have one stock account.
        return null;
    }

    /**
     * Used in serialization to store the InvestmentInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to serlialize the investment interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("InvestmentInterface writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the investment interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used  to deserialize  object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("InvestmentInterface readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("InvestmentInterface readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
