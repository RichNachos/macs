/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {	
	
	// Rectangle Width
	private static final double RECT_WIDTH = 120;
	
	//Rectangle Height
	private static final double RECT_HEIGHT = 45;
	
	public void run() {
		
		// The vertical length between Program rectangle and other rectangles
		double verticalSpacing = (getHeight() - (RECT_HEIGHT * 2)) / 3;
				
		// The horizontal length between bottom three rectangles
		double horizontalSpacing = (getWidth() - (RECT_WIDTH * 3)) / 4;
		
		//Program rectangle and label
		GRect rectProgram = new GRect(RECT_WIDTH,RECT_HEIGHT);
		GLabel labelProgram = new GLabel("Program");
		add(rectProgram, (horizontalSpacing * 2) + RECT_WIDTH, verticalSpacing);
		add(labelProgram, (horizontalSpacing * 2) + RECT_WIDTH + (RECT_WIDTH / 2) - (labelProgram.getWidth() / 2),verticalSpacing + (RECT_HEIGHT / 2) + (labelProgram.getAscent() / 2));
		
		
		//GraphicsProgram rectangle, label and line
		GRect rectGraphicsProgram = new GRect(RECT_WIDTH,RECT_HEIGHT);
		GLabel labelGraphicsProgram = new GLabel("GraphicsProgram");
		GLine lineGraphicsProgram = new GLine((horizontalSpacing * 2) + (RECT_WIDTH * 1.5), RECT_HEIGHT + verticalSpacing, horizontalSpacing + (RECT_WIDTH / 2), (verticalSpacing * 2) + RECT_HEIGHT);
		add(rectGraphicsProgram, horizontalSpacing, (verticalSpacing * 2) + RECT_HEIGHT);
		add(labelGraphicsProgram, horizontalSpacing + (RECT_WIDTH / 2) - (labelGraphicsProgram.getWidth() / 2),  (verticalSpacing * 2) + RECT_HEIGHT + (RECT_HEIGHT / 2) + (labelGraphicsProgram.getAscent() / 2));
		add(lineGraphicsProgram);
		
		
		//ConsoleProgram rectangle, label and line
		GRect rectConsoleProgram = new GRect(RECT_WIDTH,RECT_HEIGHT);
		GLabel labelConsoleProgram = new GLabel("ConsoleProgram");
		GLine lineConsoleProgram = new GLine((horizontalSpacing * 2) + (RECT_WIDTH * 1.5), RECT_HEIGHT + verticalSpacing, (horizontalSpacing * 2) + RECT_WIDTH + (RECT_WIDTH / 2), (verticalSpacing * 2) + RECT_HEIGHT);
		add(rectConsoleProgram, (horizontalSpacing * 2) + RECT_WIDTH, (verticalSpacing * 2) + RECT_HEIGHT);
		add(labelConsoleProgram, (horizontalSpacing * 2) + RECT_WIDTH + (RECT_WIDTH / 2) - (labelConsoleProgram.getWidth() / 2),  (verticalSpacing * 2) + RECT_HEIGHT + (RECT_HEIGHT / 2) + (labelConsoleProgram.getAscent() / 2));
		add(lineConsoleProgram);
		
		
		//DialogProgram rectangle, label and line
		GRect rectDialogProgram = new GRect(RECT_WIDTH,RECT_HEIGHT);
		GLabel labelDialogProgram = new GLabel("DialogProgram");
		GLine lineDialogProgram = new GLine((horizontalSpacing * 2) + (RECT_WIDTH * 1.5), RECT_HEIGHT + verticalSpacing, (horizontalSpacing * 3) + (RECT_WIDTH * 2) + (RECT_WIDTH / 2), (verticalSpacing * 2) + RECT_HEIGHT);
		add(rectDialogProgram, (horizontalSpacing * 3) + (RECT_WIDTH * 2), (verticalSpacing * 2) + RECT_HEIGHT);
		add(labelDialogProgram, (horizontalSpacing * 3) + (RECT_WIDTH * 2) + (RECT_WIDTH / 2) - (labelDialogProgram.getWidth() / 2),  (verticalSpacing * 2) + RECT_HEIGHT + (RECT_HEIGHT / 2) + (labelDialogProgram.getAscent() / 2));
		add(lineDialogProgram);
	}
	
}

