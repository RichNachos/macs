/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	public void run() {
		if (frontIsBlocked()) { // Checks if the row is only consisting on 1 position and ends the program here
			putBeeper();
		}
		else {
			fillCorners();
			while (noBeepersPresent()) {
				closeIn();
			}
		}
	}
	
	// Karel fills the corners of the rows
	private void fillCorners() {
		putBeeper();
		while(frontIsClear()) {
			move();
		}
		putBeeper();
		turnAround();
		move();
		
		if (beepersPresent()) {
			pickBeeper();
			turnAround();
			move();
		}
	}
	
	// Karel goes to the other end of the row to check if there is a beeper
	private void closeIn() {
		while (noBeepersPresent()) {
			move();
			if (beepersPresent()) {
				pickBeeper();
				turnAround();
				move();
				if (noBeepersPresent()) {
					putBeeper();
					move();
					if (beepersPresent()) {
						pickBeeper();
						turnAround();
						move();
					}
				}
			}
		}
	}
	
}
