package interfaces;

import atm.*;
import account.*;

import java.io.*;
import java.util.*;

/***
 * Class for the transaction interface that the user will see. This allows users to perform transactions.
 */
class TransactionInterface implements Serializable {
    /***
     * The ATM that this interface runs on.
     */
    private final ATM atm;
    /***
     * The scanner attribute for user inputs
     */
    private transient Scanner scanner;
    /***
     * GeneralInterfaceMethods represents a class that contains helper methods that TransactionInterface uses.
     */
    private final GeneralInterfaceMethods general;

    /***
     * Constructor for TransactionInterface.
     *
     * @param atm The ATM that this interface runs on
     */
    public TransactionInterface(ATM atm) {
        this.atm = atm;
        this.general = new GeneralInterfaceMethods(atm);
    }

    /***
     * The transaction menu that the user sees
     * @param user the user that is viewing the transaction menu
     */
    public void displayTransactionMenu(User user) {
        boolean goBack = false;
        scanner = new Scanner(System.in);

        while (!goBack) {
            printOptions();
            String option = scanner.next();
            switch (option) {
                case "1":
                    deposit(user);
                    break;
                case "2":
                    withdraw(user);
                    break;
                case "3":
                    transferIn(user);
                    break;
                case "4":
                    transferOut(user);
                    break;
                case "5":
                    payBill(user);
                    break;
                case "6":
                    goBack = true;
                    break;
                default:
                    System.out.println("There is no option " + option + ". Pick a number from 1 to 6.");
                    break;
            }
        }
    }

    /***
     * The options that that user can pick from in the transaction menu. The user is allowed to perform the following
     * transactions.
     *
     */
    private void printOptions() {
        System.out.println("Select an option:");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer In");
        System.out.println("4. Transfer Out");
        System.out.println("5. Pay Bills");
        System.out.println("6. Go back to main menu");
        System.out.println("Enter a number: ");
    }

    /***
     * Allows the user to deposit into their account. Each deposit and the amount that will be deposited is read from
     * deposits.txt and the deposit will always go into their primary chequing account.
     *
     * @param user the user that would like to perform the deposit
     */
    private void deposit(User user) {
        ArrayList<Account> chequingAccounts = general.listOfAccounts(user, "chequing");

        for (Account a : chequingAccounts) {
            Chequing account = (Chequing)a;
            if (account.primaryStatus) {
                account.deposit();
                break;
            }
        }
    }

    /***
     * Allows the user to withdraw from their account.
     *
     * @param user the user that would like to withdraw from their account
     */
    private void withdraw(User user) {
        String type = general.selectTypeOfAccount(false);
        general.printChoices(user, false, type);

        Account account = general.selectAccount("withdraw from", general.listOfAccounts(user, type));
        boolean running = true;

        while (running) {
            System.out.println("Input amount (The amount has to be a multiple of five, no cents allowed): ");
            scanner = new Scanner(System.in);
            String amount = scanner.next();
            StringBuilder amountB = new StringBuilder(amount);

            boolean valid = true;
            for(int i = 0; i < amountB.length();i++){
                if(!Character.isDigit(amountB.charAt(i))){valid = false;}}

            if(valid){

                if (Integer.valueOf(amount) % 5 == 0) {
                    account.withdraw(Integer.valueOf(amount));
                    running = false;
                }} else {
                System.out.println("The amount you entered is not possible, please try again.");
            }
        }
    }

    /***
     * Allows the user to transfer in to their account from another one of their accounts.
     *
     * @param user the user that would like to transfer in
     */
    private void transferIn(User user) {
        System.out.println("Which account do you want to transfer to?");
        String type = general.selectTypeOfAccount(false);
        general.printChoices(user, false, type);
        Account accountTo = general.selectAccount("transfer to", general.listOfAccounts(user, type));

        System.out.println("Which account do you want to transfer out from?");
        String typeTwo = general.selectTypeOfAccount(true);
        general.printChoices(user, false, typeTwo);
        Account accountFrom = general.selectAccount("transfer from", general.listOfAccounts(user, typeTwo));
        double amount = general.selectAmount();

        accountTo.transferIn(amount, accountFrom);
    }

    /***
     * Allows the user to transfer out from one of their accounts to another one of their accounts. You cannot
     * transfer out from a credit card account.
     *
     * @param user the user that would like to transfer out
     */
    private void transferOut(User user) {
        System.out.println("Which account do you want to transfer out from?");
        String type = general.selectTypeOfAccount(true);
        general.printChoices(user, false, type);
        Account accountFrom = general.selectAccount("transfer out from", general.listOfAccounts(user, type));

        System.out.println("Which account do you want to transfer to?");
        String typeTwo = general.selectTypeOfAccount(false);
        general.printChoices(user, false, typeTwo);
        Account accountTo = general.selectAccount("transfer to", general.listOfAccounts(user, typeTwo));

        double amount = general.selectAmount();

        accountFrom.transferOut(amount, accountTo);
    }

    /***
     * Allows the user to pay bill from one of their accounts to a receiver.
     *
     * @param user the user that would like to pay a bill
     */
    private void payBill(User user) {
        System.out.println("From which account would you like to pay the bill?");
        String type = general.selectTypeOfAccount(true);
        general.printChoices(user, false, type);
        Account accountFrom = general.selectAccount("pay the bill from", general.listOfAccounts(user, type));
        System.out.println("Enter the name of the receiver of the bill: ");
        scanner = new Scanner(System.in);
        String receiver = scanner.next();
        double amount = general.selectAmount();

        accountFrom.payBill(amount, receiver.trim());
    }

    /**
     * Used in serialization to store the TransactionInterface object.
     *
     * @param oos instance of the ObjectOutputStream class to write the trasnaction interface object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("TransactionInterface writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the transaction interface's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("TransactionInterface readObject Failed!");
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
        System.out.println("TransactionInterface readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
