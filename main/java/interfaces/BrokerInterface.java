package interfaces;

import atm.*;
import investments.MutualFund;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

/***
 * Class representing the broker menu that will be displayed in the interface for broker to be able to buy
 * and sell mutual funds for the ATM.
 *
 */
class BrokerInterface implements Serializable {

    /***
     * The ATM that this interface is running on.
     */
    private final ATM atm;
    /***
     * The scanner attribute that is used for the brokerto enter inputs into the function.
     */
    private transient Scanner scanner;

    /***
     * Constructor for BrokerInterface
     *
     * @param atm the atm that this atm is running on
     */
    public BrokerInterface(ATM atm) {
        this.atm = atm;
    }


    /***
     * The broker menu that the broker sees in the interface.
     *
     */
    void displayBrokerMenu(){
        String option;
        boolean logout = false;
        scanner = new Scanner(System.in);

        while (!logout) {
            System.out.println("Select an option:");
            System.out.println("1. Buy Funds");
            System.out.println("2. Sell Funds");
            System.out.println("3. Log Out");
            option = scanner.next();
            switch (option){
                case "1": {
                    buyFunds();
                    break;
                }
                case "2": {
                    sellFunds();
                    break;
                }
                case "3":{
                    logout = true;
                    break;
                }
                default:
                    System.out.println("There is no option \"" + option + "\". Please try again.");
                    break;
            }
        }
    }

    /***
     * Allows the broker to buy funds for the ATM. The broker will have to select the type of fund they want to buy,
     * enter the stock symbol and enter the amount of shares they would like to buy.
     */
    private void buyFunds() {
        MutualFund fundToBuy = listFunds();

        System.out.println("Enter the stock symbol: ");
        Scanner scanner = new Scanner(System.in);
        String symbol = scanner.next();

        System.out.println("Enter the amount of shares: ");
        String shares = scanner.next();

        if (checkIfValid(shares)){
        atm.getBroker().getMutualFundsBroker().buyStocksFund(fundToBuy, symbol, Integer.valueOf(shares));}
        else{System.out.println("Not a valid input, please try again");}
    }

    /***
     * Allows the broker to sell funds from the ATM. The broker will have to selec the type of fund they want to sell,
     * enter the stock symbol and enter the amount of shares they would like to sell.
     */
    private void sellFunds() {
        MutualFund fundToSell = listFunds();

        System.out.println("Enter the stock symbol: ");
        scanner = new Scanner(System.in);
        String symbol = scanner.next();

        System.out.println("Enter the amount of shares: ");
        String shares = scanner.next();

        if (checkIfValid(shares)){
        atm.getBroker().getMutualFundsBroker().sellStocksFund(fundToSell, symbol, Integer.valueOf(shares));}
        else{System.out.println("Not a valid input, please try again");}
    }

    /***
     * Returns true if the number of shares the broker enters is a valid amount, and false otherwise.
     *
     * @param shares the number of shares that the broker enters
     * @return a boolean: true if the number of shares the broker enters is a valid amount, and false otherwise.
     */
    private boolean checkIfValid(String shares){
       StringBuilder s = new StringBuilder(shares);
       boolean valid = true;

       for (int i = 0; i < s.length(); i++){
           if(!Character.isDigit(s.charAt(i))){valid = false;}
       }return valid;
    }

    /***
     * Allows the broker to select the type of fund they want: either low risk, medium risk or high risk.
     *
     * @return the fund that the broker chooses: either low risk fund, medium risk fund or high risk fund
     */
    private MutualFund listFunds() {
        System.out.println("Select the type of fund:");
        System.out.println("1. Low Risk Fund");
        System.out.println("2. Medium Risk Fund");
        System.out.println("3. High Risk Fund");
        System.out.println("Enter the number: ");

        scanner = new Scanner(System.in);
        String option;
        boolean validSelection = false;

        while (!validSelection) {
            option = scanner.next();
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
        // Will never return null because the above loop will keep looping until the user selects a valid option.
        return null;
    }

    /**
     * Used in serialization to store the BrokerInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to serialize the broker interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("BrokerInterface writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the broker interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("BrokerInterface readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected
     *
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("BrokerInterface readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}
