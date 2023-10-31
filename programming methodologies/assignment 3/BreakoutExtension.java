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

public class BreakoutExtension extends GraphicsProgram {

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
	
// Time between each program update - "tick"
	private static final int TICK_TIME = 8;
// Audio file for bounce sound effect
	private static final AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	
// Maximum allowed Ball speed
	private static final double MAX_VX = 3;
	private static final double MAX_VY = 3;
// Minimum allowed Ball speed
	private static final double MIN_VX = 1;
	private static final double MIN_VY = 1;
// Ball speed
	private static double VX = 0;
	private static double VY = 0;
// Speed multiplier on every brick hit
	private static final double SPEED_MULTIPLIER = 1.05;
// Points earned during the game
	private static int pointCounter = 0;
	
// GLabel to show points to the user while playing
	private static GLabel pointsLabel = new GLabel("0");
	
// Randomizer class instance
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
// Number of bricks while playing the game
	private static int bricksCounter = NBRICK_ROWS * NBRICKS_PER_ROW;

// Games backgroudn image
	private static final GImage BACKGROUND_IMAGE = new GImage("background.png");

// Background music
	private static final AudioClip music = MediaTools.loadAudioClip("music.au");
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		music.play();
		music.loop();
		initializeProgram();
		addMouseListeners();
		waitForClick();
		
		GLabel message = new GLabel("You Lost!");
		message.setColor(Color.WHITE);
		message.setFont("*-*-40");
		
		while (NTURNS > 0) {
			update();
			pause(TICK_TIME);
			if (bricksCounter == 0) {
				message.setLabel("You Won!");
				break;
			}
			
		}
		music.stop();
		add(message, WIDTH / 2 - message.getWidth() / 2, HEIGHT / 2 - message.getAscent() / 2);
		
		if (message.getLabel() == "You Lost!")	MediaTools.loadAudioClip("lose.au").play();
		else MediaTools.loadAudioClip("win.au").play();
	}
	
	// Everything the program has to do every tick
	private void update() {
		GObject collider = getCollidingObject();
		if (collider != null && collider != paddle) {
			addPoints(collider);
			bricksCounter--;
			bounceClip.play();
			remove(collider);
			VX = Math.min(VX * SPEED_MULTIPLIER, MAX_VX);
			VY = Math.min(VY * SPEED_MULTIPLIER, MAX_VY);
			paddle.setSize(Math.max(PADDLE_WIDTH / 3 * 2, paddle.getWidth() - 0.5), PADDLE_HEIGHT);
		}
		else if (collider == paddle) {
			changeBallDirection();
		}
		collidedOnWall();
		ball.move(VX,VY);
		
		if (collider == paddle) 
			bounceClip.play();
		
	}
	
	// Changes the ball direction to right if it hits the right side of paddle and respectively changes to left
	private void changeBallDirection() {
		double middlePaddleXPos = paddle.getX() + paddle.getWidth() / 2;
		double ballBottomPointXPos = ball.getX() + ball.getWidth() / 2;
		
		if (middlePaddleXPos >= ballBottomPointXPos)
			VX = -Math.abs(VX);
		else
			VX = Math.abs(VX);
	}
	// Checks if the ball collided with anything and returns that GObject, if it hasn't collided then it returns null
	private GObject getCollidingObject() {
		double xPos = ball.getX();
		double yPos = ball.getY();
		
		GObject leftCollider   =  getElementAt(xPos - 1, yPos + BALL_RADIUS);
		GObject topCollider    =  getElementAt(xPos + BALL_RADIUS,  yPos - 1);
		GObject rightCollider  =  getElementAt(xPos + BALL_RADIUS * 2 + 1, yPos + BALL_RADIUS);
		GObject bottomCollider =  getElementAt(xPos + BALL_RADIUS, yPos + BALL_RADIUS * 2 + 1);
		
		if (checkIfCollidingWithBrick(leftCollider)) {
			VX = Math.abs(VX);
			return leftCollider;
		}
		if (checkIfCollidingWithBrick(topCollider)) {
			VY = Math.abs(VY);
			return topCollider;
		}
		if (checkIfCollidingWithBrick(rightCollider)) {
			VX = -Math.abs(VX);
			return rightCollider;
		}
		if (checkIfCollidingWithBrick(bottomCollider)) {
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
			bounceClip.play();
			VX = -VX;
		}
		if (xPos <= 0) {
			bounceClip.play();
			VX = -VX;
		}
		if (yPos + BALL_RADIUS * 2 >= HEIGHT) {
			remove(ball);
			NTURNS--;
			pointCounter = Math.max(pointCounter - 5000, 0);
			pointsLabel.setLabel("" + pointCounter);
			if (NTURNS > 0) {
				drawBall();
				waitForClick();
			}
		}
		if (yPos <= 0) {
			bounceClip.play();
			VY = -VY;
		}
	}
	
	
	// Checks if the collider object returns brick or not
	private boolean checkIfCollidingWithBrick(GObject collider) {
		if (collider != null && collider != BACKGROUND_IMAGE && collider != pointsLabel) {
			return true;
		}
		else return false;
	}
	// Changes the paddle X coordinate to mouse X coordinates (e.g following the mouse)
	@Override
	public void mouseMoved(MouseEvent e) {
		double xPos = e.getX();
		xPos = Math.min(xPos, WIDTH - paddle.getWidth() / 2);
		xPos = Math.max(xPos, paddle.getHeight() / 2);
		paddle.setLocation(xPos - paddle.getWidth() / 2, paddle.getY());
	}
	
	// Start of the program, draws every figure needed for the game
	private void initializeProgram() {
		add(BACKGROUND_IMAGE,0,0);
		pointsLabel.setColor(Color.WHITE);
		add(pointsLabel,20,20);
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
		paddle.setColor(Color.WHITE);
		paddle.setFilled(true);
		add(paddle, WIDTH / 2 - PADDLE_WIDTH / 2 ,HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}
	
	// Draws the ball at the center of the window
	private void drawBall() {
		VX = rgen.nextDouble(MIN_VX, MAX_VX);
		if (rgen.nextBoolean(0.5)) VX = -VX;
		VY = rgen.nextDouble(MIN_VY, MAX_VY);
		if (rgen.nextBoolean(0.5)) VY = -VY;
		
		ball.setColor(Color.WHITE);
		ball.setFilled(true);
		add(ball, WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
	}
	
	private void addPoints(GObject collider) {
		if (collider.getColor() == Color.CYAN)
			pointCounter += 100;
		if (collider.getColor() == Color.GREEN)
			pointCounter += 200;
		if (collider.getColor() == Color.YELLOW)
			pointCounter += 300;
		if (collider.getColor() == Color.ORANGE)
			pointCounter += 400;
		if (collider.getColor() == Color.RED)
			pointCounter += 500;
		
		pointsLabel.setLabel("" + pointCounter);
	}
}

















