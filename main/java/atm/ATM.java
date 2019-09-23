package atm;

import account.*;
import bankmanager.*;
import broker.Broker;
import interfaces.*;
import subscriptions.Subscriber;
import subscriptions.AvailableSubscriptions;

import java.io.*;
import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/** This class represents an ATM machine.*/

public class ATM implements Serializable {

    /**Stores the total amount of the bills in the ATM in an array with the following order:
     *  [5 dollar bills, 10, dollar bills, 20 dollar bills, 50 dollar bills].
     */
    private final Bills bills;

    /**
     * The list of all users who can use the ATM.
     */
    private final ArrayList<User> listOfUsers;

    /**
     * The Bank Manager.
     */
    private final BankManager BM;

    /**
     * Today's date.
     */
    private final Calendar date;

    /**
     * ATM's interface displayed to users.
     */
    private final Interface interfaces;

    /**
     * The Broker responsible for investments.
     */
    private final Broker broker;

    /**
     * All available services users can currently subscribe to through the ATM.
     */
    private final AvailableSubscriptions subscriptions;

    /**
     * Handles subscriptions for users.
     */
    private final Subscriber subscriber;

    /**
     * ATM constructor.
     */
    public ATM() {
        this.interfaces = new Interface(this);
        this.BM = new BankManager(this);
        this.listOfUsers = new ArrayList<User>();
        this.date = Calendar.getInstance();
        this.date.add(Calendar.YEAR, -1);
        this.date.add(Calendar.MONTH, -3);
        this.broker = new Broker(this, BM);
        this.subscriptions = new AvailableSubscriptions();
        this.subscriber = new Subscriber(this);
        bills = new Bills(100, 100, 100, 100);
    }

    /**
     * Gets the number of each of type of bill available in the ATM.
     *
     * @return list of bills currently in the ATM
     */
    public Bills getBills() {
        return bills;
    }

    /**
     * @return the bank manager responsible for the ATM
     */
    public BankManager getBM(){
        return BM;
    }

    /**
     * @return the Broker responsible for investments done through the ATM
     */
    public Broker getBroker() {
        return broker;
    }

    /**
     *
     * @return set of services users can currently subscribe to through the ATM
     */
    public AvailableSubscriptions getSubscriptions(){
        return this.subscriptions;
    }

    /**
     *
     * @return the subsciber responsible for handling subscriptions offered by the ATM
     */
    public Subscriber getSubscriber(){
        return this.subscriber;
    }

    /**
     *
     * @return the current date used by the ATM
     */
    public Calendar getDate(){
        return (Calendar) date.clone();
    }

    /**
     *
     * @return list of all users who can use the ATM
     */
    public ArrayList<User> getListOfUsers(){
        return listOfUsers;
    }

    /**
     * Returns a user if they are recognized by the ATM.
     *
     * @param username the username of the person trying to use the ATM
     * @return the verified {@link User}
     */
    public User getUser(String username) {
        for (User usr : listOfUsers) {
            if (usr.getUsername().equals(username)) {
                return usr;
            }
        }
        // This will never happen because we have a previous function that already checks if the user is in the
        // listOfUser in Interface class.
        return null;
    }

    /**
     * Reboots the ATM. Every time the ATM is rebooted, all stocks, mutual funds and subscriptions are updated with
     * the newest prices. Furthermore, it checks adds interest to savings every month. Every time the ATM is run,
     * the login menu will be displayed. If the ATM is run for the first time, then the only two people that are allowed
     * to log in are Bank Manager and Broker with the usernames "manager" and "broker" and passwords "password" and
     * "password," respectively.
     *
     */
    public void run(){
        boolean running = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Booting on " + sdf.format(date.getTime()));
        addSavingsInterest();
        this.broker.getStockBroker().updateAllStocks();
        this.subscriber.updateAllSubscriptions();
        this.getBroker().getMutualFundsBroker().updateMutualFunds();
        while (running){
            String username = interfaces.displayLoginMenu();
            if (username.equals("manager")) {
                interfaces.displayManagerMenu(BM);
            } else if (username.equals("broker")) {
                interfaces.displayBrokerOrUserChoice();
            } else if (!username.equals("")){
                interfaces.displayUserMenu(getUser(username));
            }
        }
    }

    /**
     * Adds the specified interestRate to the balances
     * of all savings account at the beginning of each month.
     *
     * @see Savings
     */
    private void addSavingsInterest(){
        if (date.get(Calendar.DAY_OF_MONTH) == 1){
            for (User user : listOfUsers){
                ArrayList<Account> listOfAccounts = user.getAccounts();
                System.out.println(user.getUsername());
                for (Account account: listOfAccounts){
                    if (account instanceof Savings){
                        ((Savings)account).addInterest();
                    }
                }
            }
        }

    }

    /**
     * Sets the date of the ATM machine for dates specific demoing purposes.
     *
     * @param sdfFormattedDate a string that is formatted in "YYYY-MM-DD" where YYYY is the year, MM is the month
     *                         and DD is the date.
     */
    public void setDate(String sdfFormattedDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            date.setTime(sdf.parse(sdfFormattedDate));
        } catch (ParseException e){
            System.out.println("Setdate Parse Exception at ATM, this should never happen!");
            System.exit(-1);
        }
    }

    /**
     * Adds a new user to the list of users who can use the ATM.
     *
     * @param u the new user to add to the ATM's list of recognized users
     */
    public void addUserToList(User u){
        getListOfUsers().add(u);
    }

    /**
     * Shuts down the ATM. Uses serialization to store the current state of the ATM machine.
     */
    public void shutDown(){
        date.add(Calendar.DATE, 1);
        try {
            File file = new File("serialized.blob");
            file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used to serialize the ATM object.
     *
     * @param oos instance of the ObjectOutputStream class to write the ATM object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("ATM writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the ATM's state after it reboots.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize the ATM object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("ATM readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails.
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("ATM readObjectNoData, this should never happen!");
        System.exit(-1);
    }


}