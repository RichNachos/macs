/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	private static final int SENTINEL = 0;
	
	public void run() {
		int max,min;
		println("This program finds the largest and smallest numbers.");
		
		// The first number to read because we can't compare empty int's with a real number
		int tempNumber = readInt("? ");
		max = min = tempNumber; // If only one number is entered it is both the minimum AND maximum
		
		while (tempNumber != SENTINEL) { // Checks if the first number entered is the SENTINEL
			int number = readInt("? ");
			
			if (number == SENTINEL) break; // Check if the entered number is the SENTINEL and breaks loop accordingly
			
			max = Math.max(max, number); // max variable gets a new value if it is lower than the new entered number
			min = Math.min(min, number); // min variable gets a new value if it is higher than the new entered number
		}
		
		// If the first number was not SENTINEL then normally display the min and max from the entered numbers
		if (tempNumber != SENTINEL) {
			println("smallest: " + min);
			println("biggest: " + max);
		}
		else { // If the first number was SENTINEL then display that no numbers were entered
			println("No numbers entered");
		}
	}
}

