package atm;

import java.io.*;

/**
 * Represents a transaction completed by a user.
 */
public class Transaction implements Serializable {

    /**
     * Type of transaction (i.e Transfer In/Out, Deposit, etc.)
     */
    private final String type;

    /**
     * The number of the account used to perform the transaction.
     */
    private int accountNum = 0;

    /**
     * The dollar amount of the transaction.
     */
    private final double amount;

    /**
     * The name of the external account to whom a bill is being paid to.
     */
    private String billPayee = null;

    /**
     * Transaction constructor for Transfer In/Out trasnactions
     *
     * @param acctNum the unique account number for the account user used to do the transaction
     * @param amount the dollar amount of the transaction
     * @param type refers to transaction type
     *
     */
    public Transaction(int acctNum, Double amount, String type) {
        this.type = type;
        this.amount = amount;
        this.accountNum = acctNum;
    }

    /**
     * Transaction constructor for Withdraw/Deposit transactions
     *
     * @param amount the dollar amount of the transaction
     * @param type refers to transaction type

     */
    public Transaction(Double amount, String type) {
        this.type = type;
        this.amount = amount;
    }

    /**
     *Transaction constructor for Pay Bill transactions
     *
     * @param billPayee the person/account the bill is being paid to
     * @param amount the bill's dollar amount
     */
    public Transaction(String billPayee, Double amount){
        this.billPayee = billPayee;
        this.type = "paybill";
        this.amount = amount;
    }

    /**
     *
     * @return the dollar amount of the transaction
     */
    public double getTransactionAmount() {
        return this.amount;
    }

    /**
     *
     * @return type of the transaction
     */
    public String getTransactionType() {
        return this.type;
    }

    /**
     *
     * @return the unique number of the account from which the transaction was performed
     */
    public int getTransactionAccount() {
        return this.accountNum;
    }

    public String toString() {
        if (this.type.equalsIgnoreCase("transferin")) {
            return "Transferred in " + this.amount +" from account " + this.accountNum;
        } else if (this.type.equalsIgnoreCase("transferout")) {
            return "Transferred out " + this.amount + " from account: " + this.accountNum;
        } else if (this.type.equalsIgnoreCase("withdraw")) {
            return "Withdrew " + this.amount;
        } else if (this.type.equals("deposit")) {
            return "Deposited " + this.amount;
        } else {
            return "Paid " + this.amount + " to " + this.billPayee;
        }
    }

    /**
     * Used to serialize a Transaction object.
     * @param oos instance of the ObjectOutputStream class to write the Transaction object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("Transaction writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used to deserialize Transaction objects after ATM machine reboots.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize Transaction object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("Transaction readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails.
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("Transaction readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}
