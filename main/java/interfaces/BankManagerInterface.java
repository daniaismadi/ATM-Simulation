package interfaces;

import account.*;
import atm.*;
import bankmanager.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/***
 * A class representing the bank manager menu that will be displayed in the interface for bank managers to perform
 * the required tasks: set date, create user, create account, check alerts, restock machine, undo transactions
 * for users, shut down the system.
 *
 */
class BankManagerInterface implements Serializable{
    /***
     * The ATM that this interface runs on.
     */
    private final ATM atm;
    /***
     * GeneralInterfaceMethods represents a class that contains helper methods that BankManagerInterface uses.
     */
    private final GeneralInterfaceMethods general;
    /***
     * The scanner attribute that is used for the bank manager to enter inputs into the function.
     */
    private transient Scanner scanner;

    /***
     * Constructor for the BankManagerInterface.
     *
     * @param atm the atm that this interface runs on
     */
    public BankManagerInterface(ATM atm) {
        this.atm = atm;
        this.general = new GeneralInterfaceMethods(atm);
    }

    /***
     * The bank manager menu that the bank manager sees in the interface.
     * @param bm the bank manager that is viewing this interface
     */
    public void displayManagerMenu(BankManager bm){

        boolean loggedOut = false;

        while (!loggedOut){
            printOptions();
            scanner = new Scanner(System.in);
            String option = scanner.next();
            switch (option) {
                case "0": {
                    setDate();
                    break;
                }
                case "1": {
                    createUser();
                    break;
                }
                case "2": {
                    creatingAccount();
                    break;
                }
                case "3": {
                    checkAlerts();
                    break;
                }
                case "4": {
                    restockMachine(bm);
                    break;
                }
                case "5": {
                    undoTransaction();
                    break;
                }
                case "6": {
                    loggedOut = true;
                    break;
                }
                case "7":{
                    shutDownSystem();
                    break;
                }
                default: {
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 7.");
                    break;
                }
            }
        }
    }

    /***
     * The options the bank manager choose to perform in the interface.
     */
    private void printOptions() {
        System.out.println("Select an option:");
        System.out.println("0. Set Date");
        System.out.println("1. Create User");
        System.out.println("2. Create account");
        System.out.println("3. Check Alerts");
        System.out.println("4. Restock Machine");
        System.out.println("5. Undo transaction");
        System.out.println("6. Logout");
        System.out.println("7. Turn Off System");
    }

    /***
     * The method that allows the bank manager to set the date for the ATM.
     *
     */
    private void setDate() {
        boolean condition = false;
        String year = null, month = null, day = null;
        scanner = new Scanner(System.in);
        while(!condition){
            condition = true;
            System.out.println("Setting date:");
            System.out.println("Year:");
            year = scanner.next();
            System.out.println("Month:");
            month = scanner.next();
            System.out.println("Day:");
            day = scanner.next();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.parse(year + "-" + month + "-" + day);
            } catch (ParseException e){
                System.out.println("Parse failed. Is the date provided well formed?");
                System.out.println("Try again.");
                condition = false;
            }
        }
        atm.setDate(year + "-" + month + "-" + day);
        System.out.println("Date set, but note that time sensitive operations might not execute immediately " +
                "(such as addSavingsInterest, which happens after the system boots). Also note that the " +
                "date will still increment another day if you restart the system via the manager");
    }


    /***
     * Creates a new user. All account types will be automatically opened for each user when a new user is created.
     *
     */
    private void createUser(){
        System.out.println("Type the username for the new user");
        scanner = new Scanner(System.in);

        String username = scanner.next();

        User user = general.findUser(username);

        while (user != null) {
            System.out.println("Username is already taken. Please enter a new username:");
            username = scanner.next();
            user = general.findUser(username);
        }

        System.out.println("Type the password for the new user");
        String password = scanner.next();
        atm.getBM().createUser(username, password);
    }

    /***
     * Creates a new account for a certain user. The username of the user and the type of account are inputs from
     * the bank manager.
     *
     */
    private void creatingAccount() {
        scanner = new Scanner(System.in);
        User user = null;
        boolean created = false;
        int count = 0;
        int count2 = 0;
        while (user == null) {
            if (count != 0) {
                System.out.println("Type in the username of the user that would like to create an account: ");
            }
            String username = scanner.next();
            for (User parameter : atm.getListOfUsers()) {
                if (parameter.getUsername().equals(username)) {
                    user = parameter;
                    general.createAccount(user);
                    created = true;
                    break;
                }
            }
            if (count2 != 0 && !created) {
                System.out.println("The username is not valid, please try again.");
            }
            count += 1;
            count2 += 1;
        }
    }

    /***
     * Method that allows the bank manager to check alerts. The ATM will alert the bank manager every time the
     * amount of any denomination in the ATM goes below 20. This method will read from alerts.txt because
     * every time the amount of any denomination in the ATM goes below 20, the ATM will automatically write into
     * alerts.txt. The bank manager will be able to check this file through this method and this is a signal for the
     * bank manager to restock the machine.
     *
     */
    private void checkAlerts(){
        try {System.out.println(System.getProperty("user.dir"));
            File file = new File(System.getProperty("user.dir") + "/phase1/src/main/Text Files/alerts.txt");
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader r = new BufferedReader(isr);
            String line = "Alerts:";
            System.out.println(line);

            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Problem reading the file alerts.txt");
        }
    }

    /***
     * Allows the bank manager to restock the machine. The bank manager will be allowed to select the type of bill
     * to restock. The number of that type of bill the bank manager picks will increase by 100.
     *
     * @param bm the bank manager that is restocking the machine
     */
    private void restockMachine(BankManager bm){
        System.out.println("Select what type of bill to restock.");
        System.out.println("1. Five dollars, 2. Ten dollars, 3. Twenty dollars, 4. Fifty dollars");
        scanner = new Scanner(System.in);
        String dollarType = scanner.next();
        switch (dollarType) {
            case "1":
                bm.restock(1);
                break;
            case "2":
                bm.restock(2);
                break;
            case "3":
                bm.restock(3);
                break;
            case "4":
                bm.restock(4);
                break;
            default:
                System.out.println("There is no option " + dollarType + ". Pick a number from 1 to 4 or quit.");
                break;
        }
    }

    /***
     * Allows the bank manager to undo all transactions, except for deposit transactions, from all types of accounts.
     * The method will ask the bank manager which user would like to undo their transaction and ask for their account
     * number.
     *
     */
    private void undoTransaction(){
        System.out.println("Type in the name of the user that would like to undo their last transaction: ");
        scanner = new Scanner(System.in);
        String username = scanner.next();
        User user = general.findUser(username);

        while (user == null) {
            System.out.println("The username you entered is not valid. Please enter a valid username or press * to" +
                    " go back to the main menu");
            username = scanner.next();

            if (username.equals("*")) {
                break;
            }

            user = general.findUser(username);
        }

        if (!username.equals("*")) {
            String type = general.selectTypeOfAccount(false);
            general.printChoices(user, false, type);
            Account account = general.selectAccount("undo its last transaction", user.getAccounts());
            atm.getBM().undoTransaction(user, account);
        }
    }

    /***
     * The method that allows bank manager to shut down the system. When the system is shut down, all data will be
     * serialized and stored in serialized.blob.
     *
     */
    private void shutDownSystem(){
        atm.shutDown();
        System.out.println("System now shutting down");
        System.exit(0);
    }

    /**
     * Used in serialization to store the BankManagerInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to write the account interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("BMI writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the bank manager interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("BMI readObject Failed!");
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
        System.out.println("BMI readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
