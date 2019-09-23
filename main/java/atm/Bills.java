package atm;

import java.io.*;

/**
 * Bills represents the bills in the ATM machine.
 */
public class Bills implements Serializable {

    /**
     * A list that stores the number of each type of bill available in the ATM.
     */
    private final int[] bills = new int[4];

    /**
     * Bills constructor.
     * @param five number of $5 bills in the ATM
     * @param ten number of $10 bills in the ATM
     * @param twenty number of $20 bills in the ATM
     * @param fifty number of $50 bills in the ATM
     */
    Bills(int five, int ten, int twenty, int fifty) {
        bills[0] = five;
        bills[1] = ten;
        bills[2] = twenty;
        bills[3] = fifty;
    }

    /***
     * Sets the number of bills of a certain type of bill to number
     *
     * @param bill the index at which the type of bill (50, 20, 10, 5 dollar bill) is located
     * @param number the number of bills to set the type of bill to
     */
    public void setBills(int bill, int number){
        bills[bill - 1] = number;
    }

    /***
     * Returns the number of a certain type of bill the ATM has.
     *
     * @param index the index at which the type of bill is located in the bills array
     * @return the number of bills that the type of bill specified at index has
     */
    private int getNumBills(int index) {
        return bills[index];
    }

    /***
     * Returns the total amount of money the ATM has
     *
     * @return the amount of money the ATM has
     */
    public double getTotalAmount(){
        return (bills[0]*5.0 + bills[1]*10.0 + bills[2]*20.0 + bills[3]* 50.0);
    }

    /***
     * Adds a certain number of a type of bill to the ATM. Updates the bills array.
     *
     * @param index the index at which the type of bill (50, 20, 10, 5 dollar bill) is located in the bills array
     * @param number the number of bills to add to the ATM
     */
    public void addBills(int index, int number){
        bills[index] += number;
    }

    /***
     * Decreases the number of bills in the ATM.
     *
     * @param index the index at which the type of bill ($50, $20, $10, $5 bill) is located in the bills array
     * @param number the number of bills to remove from the ATM
     */
    private void removeBills(int index, int number){
        bills[index] -= number;
    }

    /***
     * Withdraws a certain amount from the ATM. This method will optimize the amount of bills to withdraw from the ATM.
     * The user will receive the smallest number of bills possible.
     *
     * @param amount the total amount the user wants to withdraw, this amount has to be an integer that is divisible by
     *              5 because you cannot withdraw cents from the ATM and there are only 5, 10, 20, 50 dollar bills
     *               available in the ATM
     */

    public void withdrawBills(double amount){
        int amountToWithdraw = (int) amount;

        amountToWithdraw = maximizeBillWithdrawal(amountToWithdraw, 50, 3);
        amountToWithdraw = maximizeBillWithdrawal(amountToWithdraw, 20, 2);
        amountToWithdraw = maximizeBillWithdrawal(amountToWithdraw, 10, 1);
        maximizeBillWithdrawal(amountToWithdraw, 5, 0);
        alertManager();

    }

    /***
     * Returns the number of a certain type of bill to withdraw from the ATM and updates bills array with the new
     * amount of bills in the ATM after withdrawal.
     *
     * <p>
     *     Depending on the amount of money that the user wants to withdraw, this method will maximize how many
     *     of a certain type of bill (either a 50, 20, 10, or 5) the ATM will withdraw.
     * </p>
     *
     *
     * @param amountToWithdraw the amount of money the user wants to withdraw from the ATM
     * @param typeOfBill the type of bill to withdraw (either a 50, 20, 10 or 5 dollar bill)
     * @param index the index where typeOfBill is stored in {@link #bills} array
     * @return the number of a certain type of bill to withdraw
     */
    private int maximizeBillWithdrawal(int amountToWithdraw, int typeOfBill, int index) {

        if (amountToWithdraw/typeOfBill > bills[index]) {
            // Go to this if statement if there are not enough bills of this typeOfBill in the ATM.
            amountToWithdraw -= bills[index] * typeOfBill;
            System.out.println("You have received " + bills[index] + " " + typeOfBill + "$ bills");
            setBills(index, 0);
        } else {
            int billsToWithdraw = (amountToWithdraw/typeOfBill);
            amountToWithdraw -= (billsToWithdraw * typeOfBill);
            removeBills(index, billsToWithdraw);
            System.out.println("You have received " + (billsToWithdraw) + " " + typeOfBill + "$ bills");
        }

        return amountToWithdraw;
    }

    /**Alerts the manager when the amount of any denomination of bills goes below 20.
     * The alert is written alerts.txt to which the Bank Manager has accessed to.
     * */
    private void alertManager() {
        boolean fiveBills = true;
        boolean tenBills = true;
        boolean twentyBills = true;
        boolean fiftyBills = true;

        if(getNumBills(0)*5 < 20){fiveBills = false;}
        if(getNumBills(1)*10 < 20){tenBills = false;}
        if(getNumBills(2)*20 < 20){twentyBills = false;}
        if(getNumBills(3)*50 < 20){fiftyBills = false;}

        try {
            //System.out.println(System.getProperty("user.dir"));
            File file = new File(System.getProperty("user.dir") + "/phase1/src/main/Text Files/alerts.txt");
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            if(!fiveBills){w.write("ALERT 5$ bills are low \n");}
            if(!tenBills){w.write("ALERT 10$ bills are low \n");}
            if(!twentyBills){w.write("ALERT 20$ bills are low \n");}
            if(!fiftyBills){w.write("ALERT 50$ bills are low \n");}
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file alert.txt");
        }
    }

    /**
     * Used to serialize the Bills object.
     * @param oos instance of the ObjectOutputStream class to serialize the Bills object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("Bills writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used to deserialize the Bills object after the ATM is rebooted.
     *
     * @param ois instance of the ObjectInputStream class used to deserialize the Bills object
     * @throws ClassNotFoundException if the class of the serialized object could not be found
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("Bills readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected*
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("Bills readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}

