package atm;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class UserTest {
    private User usr;
    private Chequing c1;
    private Savings s1;


    @Before
    public void setup(){
        ArrayList<Account> accountList = new ArrayList<>();
        usr = new User("a", "b", accountList);
        c1 = new Chequing(01);
        s1 = new Savings(02);
    }

    @Test
    public void Addaccounts(){
        assertEquals(usr.accounts.size(), 0);
        usr.accounts.add(c1);
        assertEquals(usr.accounts.size(), 1);
        usr.accounts.add(s1);
        assertEquals(usr.accounts.size(), 2);
    }
}


