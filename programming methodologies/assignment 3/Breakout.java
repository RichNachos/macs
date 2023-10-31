/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static int NTURNS = 3;

// Paddle as a global variable
	private static GRect paddle = new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
	
// Ball as a global variable
	private static GOval ball = new GOval(BALL_RADIUS*2,BALL_RADIUS*2);
	
// Ball speed
	private static double VX = 0;
	private static double VY = 0;

// Randomizer class instance
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
// Number of bricks while playing the game
	private static int bricksCounter = NBRICK_ROWS * NBRICKS_PER_ROW;
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		initializeProgram();
		addMouseListeners();
		waitForClick();
		
		GLabel message = new GLabel("You Lost!");
		
		while (NTURNS > 0) {
			update();
			pause(8);
			if (bricksCounter == 0) {
				message.setLabel("You Won!");
				break;
			}
		}
		
		add(message, WIDTH / 2 - message.getWidth() / 2, HEIGHT / 2 - message.getAscent() / 2);
	}
	
	// Everything the program has to do every tick
	private void update() {
		GObject collider = getCollidingObject();
		if (collider != null && collider != paddle) {
			remove(collider);
			bricksCounter--;
		}
		collidedOnWall();
		
		ball.move(VX,VY);
	}
	
	// Checks if the ball collided with anything and returns that GObject, if it hasn't collided then it returns null
	private GObject getCollidingObject() {
		double xPos = ball.getX();
		double yPos = ball.getY();
		
		GObject leftCollider   =  getElementAt(xPos - 1, yPos + BALL_RADIUS);
		GObject topCollider    =  getElementAt(xPos + BALL_RADIUS,  yPos - 1);
		GObject rightCollider  =  getElementAt(xPos + BALL_RADIUS * 2 + 1, yPos + BALL_RADIUS);
		GObject bottomCollider =  getElementAt(xPos + BALL_RADIUS, yPos + BALL_RADIUS * 2 + 1);
		
		if (leftCollider != null) {
			VX = Math.abs(VX);
			return leftCollider;
		}
		if (topCollider != null) {
			VY = Math.abs(VY);
			return topCollider;
		}
		if (rightCollider != null) {
			VX = -Math.abs(VX);
			return rightCollider;
		}
		if (bottomCollider != null) {
			VY = -Math.abs(VY);
			return bottomCollider;
		}
		return null;
	}
	
	// Checks if the ball collided on a wall and changes ball direction
	// Also checks if it collided with the bottom wall and subtracts total number of tries and if the player still
	// has a try left then the ball spawns back at the center and program waits for user input
	private void collidedOnWall() {
		double xPos = ball.getX();
		double yPos = ball.getY();
		
		if (xPos + BALL_RADIUS * 2 >= WIDTH) {
			VX = -VX;
		}
		if (xPos <= 0) {
			VX = -VX;
		}
		if (yPos + BALL_RADIUS * 2 >= HEIGHT) {
			remove(ball);
			NTURNS--;
			if (NTURNS > 0) {
				drawBall();
				waitForClick();
			}
		}
		if (yPos <= 0) {
			VY = -VY;
		}
	}
	
	// Changes the paddle X coordinate to mouse X coordinates (e.g following the mouse)
	@Override
	public void mouseMoved(MouseEvent e) {
		double xPos = e.getX();
		xPos = Math.min(xPos, WIDTH - PADDLE_WIDTH / 2);
		xPos = Math.max(xPos, PADDLE_WIDTH / 2);
		paddle.setLocation(xPos - PADDLE_WIDTH / 2, paddle.getY());
	}
	
	// Start of the program, draws every figure needed for the game
	private void initializeProgram() {
		drawBricks();
		drawPaddle();
		drawBall();
	}
	
	// Draws all the bricks using the above declared settings
	private void drawBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			drawBrickRow(i);
		}
	}
	
	// Draws a brick row and gives it a certain color which depends on which number of row the bricks are in
	private void drawBrickRow(int i) {
		Color color = determineColor(i);
		double yPos = BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * i;
		double xPos = BRICK_SEP / 2;
		for (int j = 0; j < NBRICKS_PER_ROW; j++) {
			GRect brick = new GRect(BRICK_WIDTH,BRICK_HEIGHT);
			brick.setColor(color);
			brick.setFilled(true);
			add(brick,xPos,yPos);
			xPos += BRICK_WIDTH + BRICK_SEP;
		}
	}
	
	// Determines the color of the bricks in "i" row
	private Color determineColor(int i) {
		if (i / 2 == 0) return Color.RED;
		if (i / 2 == 1) return Color.ORANGE;
		if (i / 2 == 2) return Color.YELLOW;
		if (i / 2 == 3) return Color.GREEN;
		return Color.CYAN;
	}
	
	// Draws the paddle
	private void drawPaddle() {
		paddle.setColor(Color.BLACK);
		paddle.setFilled(true);
		add(paddle, WIDTH / 2 - PADDLE_WIDTH / 2 ,HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}
	
	// Draws the ball at the center of the window
	private void drawBall() {
		VX = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) VX = -VX;
		VY = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) VY = -VY;
		
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball, WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
	}
}

















