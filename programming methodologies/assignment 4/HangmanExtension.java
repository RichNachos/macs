/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import java.applet.AudioClip;

import acm.graphics.*;
import acm.program.ConsoleProgram;
import acm.util.MediaTools;
import acm.util.RandomGenerator;


public class HangmanExtension extends ConsoleProgram {
	
	// Window width and height
	public static final int APPLICATION_HEIGHT = 720;
	public static final int APPLICATION_WIDTH = 1280;
	
	// Amount of tries player has each game (Don't change this)
	private static final int NUM_TRIES = 8;
	
	// Generic random generator for picking words
	private static final RandomGenerator rgen = new RandomGenerator();
	
	// Lexicon class variable to get words
	private static final HangmanLexiconExtension lex = new HangmanLexiconExtension();
	
	// Audio files 
	private static final AudioClip music = MediaTools.loadAudioClip("music.mp3");
	private static final AudioClip incorrect = MediaTools.loadAudioClip("incorrect.mp3");
	private static final AudioClip correct = MediaTools.loadAudioClip("correct.mp3");
	private static final AudioClip win = MediaTools.loadAudioClip("win.mp3");
	private static final AudioClip lose = MediaTools.loadAudioClip("lose.mp3");
	
	private HangmanCanvasExtension canvas;
	
	// Chosen word, currently uncovered word, and wrong letters which the user guessed.
	private static String WORD;
	private static String CURRENT_WORD;
	private static String GUESSED_LETTERS;
	private static int TRIES_LEFT;
	
	public void init() { 
		canvas = new HangmanCanvasExtension(); 
		add(canvas); 
	} 

    public void run() {
    	println("Welcome to Hangman!");
    	
    	music.loop();
    	music.play();
    	
		startGame();
		
		char answer = readLine("Play again? (Y/N): ").toUpperCase().charAt(0);
		while (answer == 'Y') {
			startGame();
			answer = readLine("Play again? (Y/N): ").charAt(0);
		}
		println("Thanks for playing!");
	}
    
    // Starts a single game
    private void startGame() {
    	canvas.reset();
    	WORD = getRandomWord();
    	CURRENT_WORD = hideWord(WORD);
    	canvas.displayWord(CURRENT_WORD);
    	GUESSED_LETTERS = "";
    	TRIES_LEFT = NUM_TRIES;
    	println(WORD);
    	while (TRIES_LEFT > 0) {
    		if (wonGame()) {
    			wonGameMessage();
    			break;
    		}
    		displayCurrentStatus(TRIES_LEFT);
    		guessLetter();
    	}
    	if (!wonGame()) {
    		loseGameMessage();
    	}
    }
    
    // Displays what the uncovered word is like and number of tries left
    private void displayCurrentStatus(int tries) {
    	println("Your word looks like this: " + CURRENT_WORD);
    	println("Tries left: " + tries);
    }
    
    // Gets a random word for lexicon class variable
    private String getRandomWord() {
    	return lex.getWord(rgen.nextInt(0,lex.getWordCount() - 1)).toUpperCase();
    }
    
    // Changes every character in a string to '-'
    private String hideWord(String word) {
    	String hiddenWord = "";
    	for (int i = 0; i < word.length(); i++) {
    		hiddenWord = hiddenWord + "-";
    	}
    	return hiddenWord;
    }
    
    // Checks if the letter is guessed in the word, if not then check if user guessed correctly
    private void guessLetter() {
    	char EnteredLetter = getLetter();
    	if (EnteredLetter != '-') {
    		if (notAlreadyGuessed(EnteredLetter)) {
    			checkIfGuessedLetter(EnteredLetter);
    		}
    	}
    	else {
    		println("Sorry, what you entered couldn't be read as a letter, try again.");
    	}
    }
    
    // Returns a letter if user entered normally, otherwise prints '-' to signal that user didn't input a letter
    private char getLetter() {
    	try {
			String EnteredText = readLine("Your Guess: ");
	    	char EnteredChar = EnteredText.charAt(0);
	    	if (Character.isLetter(EnteredChar)) {
	    		return Character.toUpperCase(EnteredChar);
	    	}
	    	else
	    		return '-';
		} catch (Exception e) {
			return '-';
		}
    }
    
    // Checks if the word has the guessed letter and unhides it
    private void checkIfGuessedLetter(char EnteredLetter) {
    	if (letterIsFound(EnteredLetter)) {
    		unhideLetters(EnteredLetter);
    	}
    }
    
    // If at least a single letter is found that matches users input, then return true, otherwise return false
    private boolean letterIsFound(char EnteredLetter) {
    	for (int i = 0; i < WORD.length(); i++) {
    		if (EnteredLetter == WORD.charAt(i)) {
    			correct.play();
    			return true;
    		}
    	}
    	incorrect.play();
    	TRIES_LEFT--;
    	canvas.noteIncorrectGuess(EnteredLetter);
    	return false;
    }
    
    // Checks if the user already guessed the same letter, if not return true, otherwise false
    private boolean notAlreadyGuessed(char EnteredLetter) {
    	for (int i = 0; i < GUESSED_LETTERS.length(); i++) {
    		if (EnteredLetter == GUESSED_LETTERS.charAt(i))
    			return false;
    	}
    	return true;
    }
    
    // Unhides the letters from the current word
    private void unhideLetters(char EnteredLetter) {
    	for (int i = 0; i < WORD.length(); i++) {
    		if (EnteredLetter == WORD.charAt(i)) {
    			CURRENT_WORD = CURRENT_WORD.substring(0, i) + WORD.charAt(i) + CURRENT_WORD.substring(i + 1);
    		}
    	}
    	canvas.displayWord(CURRENT_WORD);
    }
    
    // Checks if user won or lost the game and returns respectively true and false
    private boolean wonGame() {
    	if (WORD.equals(CURRENT_WORD))
    		return true;
    	return false;
    }
    // Prints message for winning
    private void wonGameMessage() {
    	println("Congratulations, you won!");
    	println("The word was: " + WORD);
    	win.play();
    }
    // Prints message for losing
    private void loseGameMessage() {
    	println("Unfortunately, you lost.");
    	println("The word was: " + WORD);
    	lose.play();
    }
}
    
    
    
    
    
    
    
    
    
    