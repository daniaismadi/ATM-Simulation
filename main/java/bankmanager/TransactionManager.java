package bankmanager;
import account.Account;
import atm.*;

import java.io.*;
import java.util.ArrayList;

/**
 * The Transaction Manager handles all requests related to {@link Transaction} as delegated by the {@link BankManager}.
 */
class TransactionManager implements Serializable {

    /**
     * Used to undo any type of transaction except Deposits because deposits cannot be reversed.
     * @param usr the user that owns the account
     * @param acct the account used to perform the transaction being reversed
     */
    public void undoTransaction(User usr, Account acct){
        if (acct.getLastTransaction() == null){
            System.out.println("No previous transactions");
        } else {
            String transactionType = acct.getLastTransaction().getTransactionType();

            if (transactionType.equalsIgnoreCase("deposit")) {
                System.out.println("Deposit Transactions Cannot be undone!");
            } else if (transactionType.equals("withdraw")) {
                undoWithdraw(acct);
            } else if (transactionType.equalsIgnoreCase("transferin")){
                undoTransferIn(usr, acct);
            } else if (transactionType.equalsIgnoreCase("transferout")) {
                undoTransferOut(usr, acct);
            } else if (transactionType.equalsIgnoreCase("paybill")){
                undoPayBill(acct);
            }
        }
    }

    /**
     * Used to undo transactions of type Withdrawal.
     *
     * @param acct the account from which money was withdrawn
     */
    private void undoWithdraw(Account acct) {
        acct.addMoney(acct.getLastTransaction().getTransactionAmount());
        removeLastTransactionFromList(acct);
    }

    /**
     * Used to undo transactions of type Transfer In.
     * @param usr the user who transferred the money
     * @param acct the account the money was transferred into
     */
    private void undoTransferIn(User usr, Account acct) {
        Account TransferAcct = null;
        for (Account acct2:usr.getAccounts()){
            if (acct2.getAccountNum() == acct.getLastTransaction().getTransactionAccount()){
                TransferAcct = acct2;
            }
        }
        if (TransferAcct != null) {
            double amount = acct.getLastTransaction().getTransactionAmount();
            acct.removeMoney(amount);
            TransferAcct.addMoney(amount);
            removeLastTransactionFromList(acct);
        }
    }

    /**
     * Used to undor transactions of type Transfer Out.
     *
     * @param usr the user who transferred the money
     * @param acct the account the money was transferred out of
     */
    private void undoTransferOut(User usr, Account acct) {
        // TransferAct refers to the account that was transferred from.
        Account TransferAcct = null;
        for (Account acct2 : usr.getAccounts()) {
            if (acct2.getAccountNum() == acct.getLastTransaction().getTransactionAccount()) {
                TransferAcct = acct2;
            }
        }
        if (TransferAcct != null) {
            double amount = acct.getLastTransaction().getTransactionAmount();
            acct.addMoney(amount);
            TransferAcct.removeMoney(amount);
            removeLastTransactionFromList(acct);
        }
    }

    /**
     * Used to undo transactions of type Pay Bill.
     *
     * @param acct the account used to pay a bill from
     */
    private void undoPayBill(Account acct) {
        acct.addMoney(acct.getLastTransaction().getTransactionAmount());
        removeLastTransactionFromList(acct);
    }

    /**
     * Deletes the last {@link Transaction} from the account's list of transactions
     *
     * @param account the account the transaction is being removed from
     */
    private void removeLastTransactionFromList(Account account) {
        ArrayList<Transaction> lst = account.getListOfTransactions();
        lst.remove(lst.size() - 1);
    }


    /**
     * Used in serialization to store the Transaction Manager object.
     *
     * @param oos instance of the ObjectOutputStream class to write the transaction manager object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("BM writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the Transcation Manager's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the transaction manager object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("BM readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("BM readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
