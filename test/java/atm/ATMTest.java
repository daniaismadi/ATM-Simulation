package atm;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;


class ATMTest {

    private void reset() {
        // Switch the atm variables to private
        // change the Bankmanager file path to "/Text Files/bankmanager.txt"

        //uncomment the below code.

//        ATM.bills = new int[4];
//        ATM.listOfUsers = new ArrayList<User>();
//        ATM.BM = new bankmanager();
//        ATM.date = Calendar.getInstance();

    }

    @Before
    public void runmain() {
        reset();

        for (int i = 0; i < 4; i++) {
            ArrayList<Account> ls = new ArrayList<>();
            User usr = new User("a" + i, "b", ls);
            Chequing c1 = new Chequing(1 + i);
            Savings s1 = new Savings(2 + i);
            usr.accounts.add(c1);
            usr.accounts.add(s1);
            //ATM.listOfUsers.add(usr); uncomment

        }
        //System.out.println(ATM.getListOfUsers().size());
        ATM.shutdown();
        reset();

    }

    @Test
    public void testshutdown() {
        ATM.Restart();
    }

}



