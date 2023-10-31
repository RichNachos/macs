/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {

	public void run() {
		MoveToNewspaper(); 
		PickUpNewspaper(); 
		MoveBackToStartPos(); 
	}
	
	// Move to newspaper position
	private void MoveToNewspaper() {
		turnRight();
		move();
		turnLeft();
		move();
		move();
		move();
	}
	
	// Pick up the newspaper
	private void PickUpNewspaper() {
		pickBeeper();
	}
	
	// Get back to starting position
	private void MoveBackToStartPos() {
		turnAround();
		move();
		move();
		move();
		turnRight();
		move();
		turnRight();
	}
}
