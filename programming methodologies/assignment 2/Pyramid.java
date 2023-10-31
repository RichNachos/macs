/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	public void run() {
		double windowHeight = getHeight();
		double windowWidth = getWidth();
		for (int i = 0; i < BRICKS_IN_BASE; i++) { // Pyramid creating loop
			
			double rowWidth = (BRICKS_IN_BASE - i) * BRICK_WIDTH; // Width of the current row of bricks
			
			double positionX =  (windowWidth - rowWidth) / 2; // First X position of current row brick
			double positionY = windowHeight - (BRICK_HEIGHT * (i + 1)); // Current row's Y position
			
			for (int j = 0; j < BRICKS_IN_BASE - i; j++) { // Loop that fills current row with bricks at same Y position
				
				GRect brick = new GRect(BRICK_WIDTH,BRICK_HEIGHT);
				add(brick,positionX,positionY);
				positionX += BRICK_WIDTH; // Next brick X position
				
			}
		}
	}
}

