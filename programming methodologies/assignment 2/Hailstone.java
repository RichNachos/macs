/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int n = readInt("Enter the number: ");
		int counter = 0;
		
		if (n <= 0) {
			println("Number is negative or zero");
		}
		else {
			while (n != 1) { // Loop stops once number reaches 1
				if (checkIfEven(n)) {
					println(n + " is even so I make half: " + (n/2) );
					n /= 2;
				}
				else {
					println(n + " is odd so I make 3n + 1: " + ((3*n)+1) );
					n = (3 * n) + 1;
				}
				counter++; // Adds one to count total operations done
			}
			println("Process took " + counter + " to reach 1");
		}
	}
	
	// Checks if a given number is even and returns true or false accordingly 
	private static boolean checkIfEven(int n) {
		if (n % 2 == 0) {
			return true;
		}
		else {
			return false;
		}
	}
}

