package atm;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


class AccountTest {

    private Account accountCh;
    private Account accountS;
    private Account accountCr;
    private Account accountL;

    @Before
    public void setUp() {
        accountCh = new Chequing(000);
        accountS = new Savings(001);
        accountCr = new CreditCard(002);
        accountL = new LOC(003);
    }

    @Test
    public void Alert() {
        ATM.set_bills(0,100);
        ATM.set_bills(1,100);
        ATM.set_bills(2,100);
        ATM.set_bills(3,100);
        accountCh.addMoney(10500.00);
        accountCh.withdraw(8500.00);
        System.out.println(ATM.get_amount());

        assertEquals(2000.00, accountCh.getBalance(), 0.0);
        //double balance = account.getBalance();
        //assertEquals(1000, balance, 0.0);
    }

    @Test
    public void testPayBillWriting() {
        accountCh.addMoney(1000.00);
        boolean variable = accountCh.payBillWriting(100.00,"Test1");
        assertTrue(variable);
        //double balance = account.getBalance();
        //assertEquals(1000, balance, 0.0);
    }

    @Test
    public void testDeposit() {
        accountCh.deposit();

        assertEquals(55.02, accountCh.getBalance(), 0.0);
        //double balance = account.getBalance();
        //assertEquals(1000, balance, 0.0);
    }

   @Test
    public void testWithdraw() {
        ATM.set_bills(0,100);
        ATM.set_bills(1,100);
        ATM.set_bills(2,100);
        ATM.set_bills(3,100);
        accountCh.addMoney(500.00);
        accountCh.withdraw(285.00);

        assertEquals(215.00, accountCh.getBalance(), 0.0);
        //double balance = account.getBalance();
        //assertEquals(1000, balance, 0.0);
    }

    @Test
    public void PayBill() {
        accountS.addMoney(100.00);
        accountS.payBill(150.0, "csc207");

        assertEquals(100.0, accountS.getBalance(), 0.0);
    }

    @Test
    public void TransferIn() {
        accountCr.addMoney(-500.00);
        accountCh.addMoney(500.00);
        accountCr.transferIn(285.00,  accountCh);

        assertEquals(215.00, accountCr.getBalance(), 0.0);
    }

    @Test
    public void TransferOut() {
        accountCh.addMoney(500.00);
        accountS.addMoney(500.00);
        accountCh.transferOut(285.00,  accountS);

        assertEquals(785.00, accountS.getBalance(), 0.0);
        assertEquals(215.00, accountCh.getBalance(), 0.0);
    }
}
