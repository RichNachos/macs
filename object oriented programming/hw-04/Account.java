// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private final int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private final Bank bank;
	public int getId() { return id; }
	public int getBalance() { return balance; }
	public int getTransactions() { return transactions; }
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	public synchronized void changeBalance(int delta) {
		balance += delta;
		transactions++;
	}

	@Override
	public String toString() {
		return "acct:" + id + " bal:" + balance + " trans:" + transactions;
	}
}
