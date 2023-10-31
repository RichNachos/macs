/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {
	public static final double CM_TO_PIXEL_RATIO = 72 / 2.54;
	
	public void run() {
		// Creates GOval objects (circles)
		GOval outerCircle = new GOval(convertCmToPixels(2.54), convertCmToPixels(2.54));
		GOval middleCircle = new GOval(convertCmToPixels(1.65), convertCmToPixels(1.65));
		GOval innerCircle = new GOval(convertCmToPixels(0.76), convertCmToPixels(0.76));
		
		// Gives circles their colours
		outerCircle.setColor(Color.red);    outerCircle.setFilled(true);
		middleCircle.setColor(Color.white); middleCircle.setFilled(true);
		innerCircle.setColor(Color.red);    innerCircle.setFilled(true);
		
		// Draws all three circles
		add(outerCircle,(getWidth() - convertCmToPixels(2.54)) / 2, (getHeight() - convertCmToPixels(2.54)) / 2);
		add(middleCircle,(getWidth() - convertCmToPixels(1.65)) / 2, (getHeight() - convertCmToPixels(1.65)) / 2);
		add(innerCircle,(getWidth() - convertCmToPixels(0.76)) / 2, (getHeight() - convertCmToPixels(0.76)) / 2);
	}
	
	// Converts centimeters to pixels using a constant ratio value
	private static double convertCmToPixels(double n) {
		return n * CM_TO_PIXEL_RATIO;
	}
}
