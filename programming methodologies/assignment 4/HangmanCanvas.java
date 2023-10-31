/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;
import acm.util.ErrorException;

public class HangmanCanvas extends GCanvas {


	
	private static GLabel GUESSED_WORD = new GLabel("");
	private static GLabel INCORRECT_GUESSES = new GLabel("");
	private static int TRIES_LEFT = 8;
/** Resets the display so that only the scaffold appears */
// Also resets the labels
	public void reset() {
		removeAll();
		GUESSED_WORD.setFont("*-*-40");
		INCORRECT_GUESSES.setFont("*-*-20");
		INCORRECT_GUESSES.setLabel("");
		TRIES_LEFT = 8;
		
		STARTX = getWidth() / 2 - BEAM_LENGTH;
		STARTY = getHeight() / 2 - SCAFFOLD_HEIGHT / 2 - MARGIN * 2 - (int)GUESSED_WORD.getAscent() - (int)INCORRECT_GUESSES.getAscent();
		MIDDLEX = getWidth() / 2;
		
		drawScaffolding();
		drawLabels();
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		GUESSED_WORD.setLabel(word);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		drawBodyPart();
		INCORRECT_GUESSES.setLabel(INCORRECT_GUESSES.getLabel() + letter);
		TRIES_LEFT--;
	}
	
	// Is only called from noteIncorrectGuess method. Draws respective body part depending on which try the user is
	private void drawBodyPart() {
		switch (TRIES_LEFT) {
			case 0: break;
			case 1: drawRightFoot();
				break;
			case 2: drawLeftFoot();
				break;
			case 3: drawRightLeg();
				break;
			case 4: drawLeftLeg();
				break;
			case 5: drawRightArm();
				break;
			case 6: drawLeftArm();
				break;
			case 7: drawBody();
				break;
			case 8: drawHead();
				break;
			default: throw new ErrorException("Illegal index");
		}
	}
	
	// Draws scaffolding at the start of the game
	private void drawScaffolding() {
		GLine scaffold = new GLine(STARTX, STARTY, STARTX, STARTY + SCAFFOLD_HEIGHT);
		add(scaffold);
		
		GLine beam = new GLine(STARTX, STARTY, MIDDLEX, STARTY);
		add(beam);
		
		GLine rope = new GLine(MIDDLEX, STARTY, MIDDLEX, STARTY + ROPE_LENGTH);
		add(rope);
	}
	
	// Body part drawing methods
	private void drawHead() {
		GOval head = new GOval(HEAD_RADIUS * 2, HEAD_RADIUS * 2);
		add(head, MIDDLEX - HEAD_RADIUS, STARTY + ROPE_LENGTH);
	}
	
	private void drawBody() {
		GLine body = new GLine(MIDDLEX, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2, MIDDLEX, STARTY+ ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH);
		add(body);
	}
	
	private void drawLeftArm() {
		GLine arm = new GLine(MIDDLEX, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD, MIDDLEX - UPPER_ARM_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD);
		add(arm);
		
		GLine hand = new GLine(MIDDLEX - UPPER_ARM_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD, MIDDLEX - UPPER_ARM_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(hand);
	}
	
	private void drawRightArm() {
		GLine arm = new GLine(MIDDLEX, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD, MIDDLEX + UPPER_ARM_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD);
		add(arm);
		
		GLine hand = new GLine(MIDDLEX + UPPER_ARM_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD, MIDDLEX + UPPER_ARM_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(hand);
	}
	
	private void drawLeftLeg() {
		GLine hip = new GLine(MIDDLEX, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH, MIDDLEX - HIP_WIDTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH);
		add(hip);
		
		GLine leg = new GLine(MIDDLEX - HIP_WIDTH, STARTY+ ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH, MIDDLEX - HIP_WIDTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH);
		add(leg);
	}
	
	private void drawRightLeg() {
		GLine hip = new GLine(MIDDLEX, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH, MIDDLEX + HIP_WIDTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH);
		add(hip);
		
		GLine leg = new GLine(MIDDLEX + HIP_WIDTH, STARTY+ ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH, MIDDLEX + HIP_WIDTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH);
		add(leg);
	}
	
	private void drawLeftFoot() {
		GLine foot = new GLine(MIDDLEX - HIP_WIDTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH, MIDDLEX - HIP_WIDTH - FOOT_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH);
		add(foot);
	}
	
	private void drawRightFoot() {
		GLine foot = new GLine(MIDDLEX + HIP_WIDTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH, MIDDLEX + HIP_WIDTH + FOOT_LENGTH, STARTY + ROPE_LENGTH + HEAD_RADIUS * 2 + BODY_LENGTH + LEG_LENGTH);
		add(foot);
	}
	
	// Draws the correct and incorrect guesses labels
	private void drawLabels() {
		add(GUESSED_WORD, STARTX, STARTY + MARGIN + SCAFFOLD_HEIGHT + GUESSED_WORD.getAscent());
		add(INCORRECT_GUESSES, STARTX, STARTY + MARGIN * 2 + SCAFFOLD_HEIGHT + GUESSED_WORD.getAscent() + INCORRECT_GUESSES.getAscent());
	}
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	// Margin between labels and scaffold
	private static final int MARGIN = 20;
	
	// Used for easy construction of body parts and scaffold
	private static int STARTY = 0;
	private static int STARTX = 0;
	private static int MIDDLEX = 0;
}
	
	
