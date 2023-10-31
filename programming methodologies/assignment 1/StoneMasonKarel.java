/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	public void run() {
		while (frontIsClear()) { // If the east side is clear that means there is a next column.
			fillColumn(); 
			moveToNextColumn();
		}
		fillColumn(); // This is the last column to fill, that is why there is no moveToNextColumn() method
	}
	
	// Fill the current column on which Karel is standing on & get back to the bottom of the column
	private void fillColumn() {
		moveUpAndFill(); 
		moveDown(); 
	}
	
	// Karel climbs up the column filling in the brick gaps
	private void moveUpAndFill() {
		turnLeft();
		while(frontIsClear()) {
			if (noBeepersPresent()) {
				putBeeper();
				move();
			}
			else {
				move();
			}
		}
		if (!beepersPresent()) {
			putBeeper();
		}
		turnRight();
	}
	
	// After filling up karel comes back down to starting position on the column
	private void moveDown() {
		turnRight();
		while (frontIsClear()) {
			move();
		}
		turnLeft();
	}
	
	// After filling column Karel moves forward to the next pillar
	private void moveToNextColumn() {
		move();
		move();
		move();
		move();
	}
}
