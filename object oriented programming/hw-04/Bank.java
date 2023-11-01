// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Bank {
	public static final int ACCOUNTS = 20;	 // default number of accounts
	public static final int CASH = 1000;// default cash in each account
	private static BlockingQueue<Transaction> queue;
	private List<Account> accounts;
	private List<Worker> workers;
	private final Transaction nullTransaction = new Transaction(-1,0,0);
	private CountDownLatch latch;

	// Bank constructor
	public Bank() {
		queue = new ArrayBlockingQueue<>(ACCOUNTS);
		accounts = new ArrayList<>();
		initAccounts();
	}

	// Initializes new accounts with default cash and respective ID's
	private void initAccounts() {
		for (int i = 0; i < ACCOUNTS; i++) {
			accounts.add(new Account(this, i, CASH));
		}
	}
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	private void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				
				// YOUR CODE HERE
				Transaction newTransaction = new Transaction(from, to, amount);
				queue.put(newTransaction);
			}

			queue.add(nullTransaction);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) throws InterruptedException {
		// Start workers
		latch = new CountDownLatch(numWorkers);
		for (int i = 0; i < numWorkers; i++) {
			new Worker().start();
		}

		// Read file
		readFile(file);

		// Wait for finish
		latch.await();
	}
	public String printInformation() {
		StringBuilder builder = new StringBuilder();
		for (Account acc : accounts) {
			builder.append(acc.toString() + "\n");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	private class Worker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Transaction currentTransaction = queue.take();
					if (currentTransaction.equals(nullTransaction)) {
						queue.add(nullTransaction);
						latch.countDown();
						return;
					}
					accounts.get(currentTransaction.from).changeBalance(-currentTransaction.amount);
					accounts.get(currentTransaction.to).changeBalance(currentTransaction.amount);
				} catch (Exception e) {
					// ???
				}
			}

		}
	}
	
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		// YOUR CODE HERE
		Bank myBank = new Bank();
		myBank.processFile(file, numWorkers);
		System.out.println(myBank.printInformation());
	}
}

