package broker;

import account.Account;
import atm.ATM;
import atm.User;
import investments.MutualFund;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * Class for a user mutual fund broker who deals with buying and selling stocks for users.
 */
public class UserMutualFundBroker implements Serializable {
    /***
     * The bank mutual fund broker.
     */
    private final BankMutualFundBroker bankMutualFundBroker;

    /***
     * Constructor for the user mutual fund broker.
     *
     * @param atm The ATM that this broker is working in.
     */
    public UserMutualFundBroker(ATM atm) {
        this.bankMutualFundBroker = new BankMutualFundBroker(atm);
    }

    /***
     * Calculates the broker fee for buying this mutual fund.
     *
     * @param amount the amount that the mutual fund is bought for
     * @return returns the broker free
     */
    private double calculateBrokerFree(double amount){
        double fee = amount / 100;
        return fee;
    }

    /***
     * Allows the user to sell mutual funds.
     *
     * @param user the user that wants to sell mutual funds
     * @param fund the fund that the user wants to sell
     * @param amount the amount that the user wants to sell
     */
    public void sellMutualFunds(User user, MutualFund fund, double amount){
        double currentInvestment = calculateUserMoney(user, fund);
        if(amount <= currentInvestment){
            double sold = (-1 * amount) / 2;
            updateFundInvestors(user, fund, sold);
            for (Account account: user.getAccounts()){
                if (account.getType().equals("stock")){
                    account.addMoney(amount);
                    System.out.println("You have sold " + amount + "$ of your " + fund.getName() + " fund investment");
                    break;
                }
            }
        }else{System.out.println("\nNot enough funds to sell");}


    }

    /***
     * Calculates how much money the user's investment into a certain fund is worth.
     *
     * @param user the user that would like to see how much their investment is worth
     * @param fund the fund that the user has invested in
     * @return the money that user's investment is worth
     */
    private double calculateUserMoney(User user, MutualFund fund){
        HashMap<MutualFund, ArrayList<Double>> portfolio = user.getInvestmentPortfolio().getMutualFundPortfolio();
        double percentOwned = portfolio.get(fund).get(1);
        double fundTotalValue = fund.getValue();
        return (fundTotalValue / 100) * percentOwned;
    }


    /***
     * Allows a user to invest/buy into a mutual fund.
     *
     * @param user the user that would like to buy into a mutual fund
     * @param fund the fund that the user would like to buy into
     * @param amount the amount that the user wants to buy
     */
    public void buyMutualFunds(User user, MutualFund fund, double amount){
        double total = calculateBrokerFree(amount) + amount;
        boolean enoughStockBalance = false;
        for (Account account: user.getAccounts()){
            if (account.getType().equals("stock")){
                enoughStockBalance = account.checkFundsSufficient(total);
                break;}
        }
        if(enoughStockBalance){
            refillToSell(user, fund, amount);
        }else{
            System.out.println("\nNot enough funds in your stock account");
        }
    }

    /***
     * Alerts the bank/broker to buy more stock into a fund so the user will be able to invest more money because
     * there are currently not enough stocks in the fund.
     *
     * @param user the user that would like to invest
     * @param fund the mutual fund the stock is being added to
     * @param amount the dollar amount equivalent of stock shares to buy
     */
    private void refillToSell(User user, MutualFund fund, double amount){
        if (!possibleToBuy(fund, amount)){
            bankMutualFundBroker.refillFunds(fund, amount);}
        updateFundInvestors(user, fund, amount);
        for (Account account: user.getAccounts()){
            if (account.getType().equals("stock")){
                account.removeMoney(amount);
                System.out.println("The broker has invested " + amount + "$ into " + fund.getName() + " fund for you");
                break;
            }
        }

    }

    /***
     * Checks the percentage of the fund that has been bought.
     *
     * @param fund the fund that is to be checked
     * @param amount the amount that is bought
     * @return true if it is possible to buy this amount and false otherwise
     */
    private boolean possibleToBuy(MutualFund fund, double amount) {
        if (fund.getValue() < amount) {
            return false;
        } else {
            double totalPercent = 0.0;
            double percentOfFund = fund.getValue() / amount;
            for (User user : fund.getInvestors().keySet()) {
                totalPercent += fund.getInvestors().get(user).get(1);
            }
            return (totalPercent + percentOfFund) <= 100;
        }
    }


    /***
     * Updates information about a user's purchase in their investment portfolio and stores the users info into the
     * fund's information.
     *
     * @param user the user that has purchased a fund
     * @param fund the fund that the user has purchased into
     * @param amount the amount that the user has purchased
     */
    private void updateFundInvestors(User user, MutualFund fund, double amount){
        double percentOfFund = amount / fund.getValue() * 100;
        boolean found = findFundInvestors(user, fund, (amount /2));
        if(!found){
            ArrayList<Double> investment = new ArrayList<>();
            investment.add(amount);
            investment.add(percentOfFund);
            user.getInvestmentPortfolio().setMutualFundsPortfolio(fund, investment);
            fund.setInvestors(user, investment);}
    }

    /***
     * Returns true if user has already invested in a fund and the fund has updated accordingly.
     *
     * @param user the user that has invested
     * @param fund the fund that the user has invested into
     * @param amount the amount that the user has invested
     * @return true if the user has successfully invested in a fund and the fund has updated accordingly, false otherwise
     */
    private boolean findFundInvestors(User user, MutualFund fund, double amount){
        double percentOfFund = amount / fund.getValue() * 100;
        HashMap<MutualFund, ArrayList<Double>> userInvestments = user.getInvestmentPortfolio().getMutualFundPortfolio();
        boolean found = false;
        for (MutualFund userFund : userInvestments .keySet()){
            if(userFund.equals(fund)){
                userInvestments.get(userFund).set(0, userInvestments.get(userFund).get(0) + amount);
                userInvestments.get(userFund).set(1, userInvestments.get(userFund).get(1) + percentOfFund);
                fund.getInvestors().get(user).set(0, fund.getInvestors().get(user).get(0) + amount);
                fund.getInvestors().get(user).set(1, fund.getInvestors().get(user).get(1) + percentOfFund);
                found = true;
                break;}}
        return found;
    }

    /***
     * Calculates the percentage profit or percentage loss of the user's mutual funds investment portfolio.
     *
     * @param user the user that has invested
     * @return the percentage profit made or percentage profit loss
     */
    //Calculate the %profit or loss of the user's investmentPortfolio in mutual funds
    private double calculateInvestmentIncrease(User user){
        double invested = 0.0;
        double netWorth = 0.0;
        for (MutualFund fund : user.getInvestmentPortfolio().getMutualFundPortfolio().keySet()){
            invested += user.getInvestmentPortfolio().getMutualFundPortfolio().get(fund).get(0);
            netWorth += (fund.getValue() * (user.getInvestmentPortfolio().getMutualFundPortfolio().get(fund).get(1) / 100));
        } return ((netWorth - invested)/ invested) * 100;
    }

    /***
     * Returns a string of the funds the user has invested in and how much their invested is worth currently.
     *
     * @param user the user that has invested
     * @return the string of the mutual funds
     */
    // prints the funds the user invested in and how much their investment is worth currently
    public String toString(User user){
        String mutualFundInvestments = "";
        double total = 0.0;
        for (MutualFund fund : user.getInvestmentPortfolio().getMutualFundPortfolio().keySet()){
            double value = fund.getValue() * (user.getInvestmentPortfolio().getMutualFundPortfolio().get(fund).get(1) / 100);
            mutualFundInvestments += "\n Your mutual fund investment in " + fund.getName()
                    + " is worth " + value + "$";
            total += value;
        }
        mutualFundInvestments += "\n The total value of your mutual fund investmentPortfolio is $" + total;
        mutualFundInvestments += "\n Your total mutual fund investment increase is " +
                calculateInvestmentIncrease(user) + " $";
        return mutualFundInvestments;
    }

    /**
     * Used to serialize the UserMutualFundBroker object.
     *
     * @param oos instance of the ObjectOutputStream class to write the UserMutualFundBroker object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Used to deserialize an UserMutualFundBroker object to store the UserMutualFundBroker's information after the ATM is rebooted.
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
