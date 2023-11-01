// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private String foundWord;

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	public String generateHash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(input.getBytes());
			byte[] byteArray = md.digest();
			return hexToString(byteArray);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String crack(String input, int maxLength, int workers) {
		CountDownLatch latch = new CountDownLatch(1);
		int gap = CHARS.length / workers;

		List<Worker> workerList = new ArrayList<>();
		for (int i = 0; i < workers; i++) {
			if (workers % 2 == 1 && i == workers - 1) {
				workerList.add(new Worker(input, maxLength, i * gap, (i + 1) * gap + 1, latch));
			} else {
				workerList.add(new Worker(input, maxLength, i * gap, (i + 1) * gap, latch));
			}
		}

		for (Worker w : workerList) w.start();
		try {
			latch.await();
			for (Worker w : workerList) w.interrupt();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return foundWord;
	}

	private class Worker extends Thread {
		private final String Hash;
		private final int MaxLength;
		private final int From;
		private final int To;
		private final CountDownLatch Latch;
		public Worker(String hash, int maxLength, int from, int to, CountDownLatch latch) {
			this.Hash = hash;
			this.MaxLength = maxLength;
			this.From = from;
			this.To = to;
			this.Latch = latch;
		}
		@Override
		public void run() {
			for (int i = From; i < To; i++) {
				search("" + CHARS[i]);
			}
		}
		public void search(String soFar) {
			//System.out.println(soFar + " " + generateHash(soFar));
			if (Hash.equals(generateHash(soFar))) {
				foundWord = soFar;
				Latch.countDown();
				return;
			}
			if (soFar.length() >= MaxLength) return;

			for (char c : CHARS) {
				if (this.isInterrupted()) break;
				search(soFar + c);
			}
		}
	}

	public static void main(String[] args) {
		Cracker cracker = new Cracker();
		if (args.length < 2) {
			//System.out.println("Args: target length [workers]");
			System.out.println(cracker.generateHash(args[0]));
			return;
			//System.exit(1);
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		
		// YOUR CODE HERE
		System.out.println(cracker.crack(targ, len, num));
	}
}
