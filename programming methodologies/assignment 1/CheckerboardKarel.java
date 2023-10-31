/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	public void run() {
		putBeeper(); // The first beeper to put (1,1 position)
		while (frontIsClear()) {
			fillColumn(); 
			moveDown(); 
			moveToNextColumn(); 
		}
		fillColumn(); // The last column Karel has to fill
	}
	
	// Karel fills the column with checkerboard pattern beepers
	private void fillColumn() {
		turnLeft();
		while (frontIsClear()) {
			if (beepersPresent()) { 
				move(); // If there is a beeper on the current position then just move up
			}
			else { // Else if there is no beeper on the current position then move up AND put a beeper
				move();
				putBeeper();
			}
		}
	}
	
	// Karel comes back down to column start after filling the column
	private void moveDown() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnLeft();
	}
	
	// Karel goes to the next column while also checking whether or not to place the first beeper on first column position
	private void moveToNextColumn() {
		if (beepersPresent()) {
			move();
		}
		else {
			move();
			putBeeper();
		}
	}
}
