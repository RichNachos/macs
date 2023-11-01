import junit.framework.TestCase;
import org.junit.Test;

public class BankTest extends TestCase {

    public void testAccount() {
        Account acc = new Account(new Bank(), 0, 1000);
        assertEquals(1000, acc.getBalance());
        assertEquals(0, acc.getId());
        assertEquals(0, acc.getTransactions());
        acc.changeBalance(10);
        acc.changeBalance(-20);
        assertEquals(990,acc.getBalance());
        assertEquals("acct:0 bal:990 trans:2", acc.toString());
    }

    public void testBank() throws InterruptedException {
        Bank myBank = new Bank();
        assertEquals("acct:0 bal:1000 trans:0\n" +
                "acct:1 bal:1000 trans:0\n" +
                "acct:2 bal:1000 trans:0\n" +
                "acct:3 bal:1000 trans:0\n" +
                "acct:4 bal:1000 trans:0\n" +
                "acct:5 bal:1000 trans:0\n" +
                "acct:6 bal:1000 trans:0\n" +
                "acct:7 bal:1000 trans:0\n" +
                "acct:8 bal:1000 trans:0\n" +
                "acct:9 bal:1000 trans:0\n" +
                "acct:10 bal:1000 trans:0\n" +
                "acct:11 bal:1000 trans:0\n" +
                "acct:12 bal:1000 trans:0\n" +
                "acct:13 bal:1000 trans:0\n" +
                "acct:14 bal:1000 trans:0\n" +
                "acct:15 bal:1000 trans:0\n" +
                "acct:16 bal:1000 trans:0\n" +
                "acct:17 bal:1000 trans:0\n" +
                "acct:18 bal:1000 trans:0\n" +
                "acct:19 bal:1000 trans:0", myBank.printInformation());
        myBank.processFile("small.txt", 4);
        assertEquals("acct:0 bal:999 trans:1\n" +
                "acct:1 bal:1001 trans:1\n" +
                "acct:2 bal:999 trans:1\n" +
                "acct:3 bal:1001 trans:1\n" +
                "acct:4 bal:999 trans:1\n" +
                "acct:5 bal:1001 trans:1\n" +
                "acct:6 bal:999 trans:1\n" +
                "acct:7 bal:1001 trans:1\n" +
                "acct:8 bal:999 trans:1\n" +
                "acct:9 bal:1001 trans:1\n" +
                "acct:10 bal:999 trans:1\n" +
                "acct:11 bal:1001 trans:1\n" +
                "acct:12 bal:999 trans:1\n" +
                "acct:13 bal:1001 trans:1\n" +
                "acct:14 bal:999 trans:1\n" +
                "acct:15 bal:1001 trans:1\n" +
                "acct:16 bal:999 trans:1\n" +
                "acct:17 bal:1001 trans:1\n" +
                "acct:18 bal:999 trans:1\n" +
                "acct:19 bal:1001 trans:1", myBank.printInformation());
    }
}
