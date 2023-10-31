/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.Arrays;

import com.sun.org.apache.bcel.internal.generic.GOTO;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerScores[i] = 0;
			lowerScores[i] = 0;
			upperScores[i] = 0;
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
			for (int j = 0; j < 18; j++)
				chosenCategories[i][j] = false;
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		for (int turn = 1; turn <= 13; turn++) {
			for (int playerTurn = 1; playerTurn <= nPlayers; playerTurn++) {
				display.printMessage(playerNames[playerTurn - 1] + "'s turn. Click Roll Dice button to roll dice.");
				display.waitForPlayerToClickRoll(playerTurn);
				int[] dice = getRandomDice();
				display.displayDice(dice);
				
				for (int i = 0; i < 2; i++) {
					display.printMessage("Select the dice you wish to re-roll and click 'Roll Again'.");
					display.waitForPlayerToSelectDice();
					dice = randomizeSelectedDice(dice);
					display.displayDice(dice);
				}
				
				display.printMessage("Select a category for this roll.");
				int catNum = display.waitForPlayerToSelectCategory();
				
				while (chosenCategories[playerTurn][catNum]) {
					display.printMessage("You have already chosen that category, choose another one.");
					catNum = display.waitForPlayerToSelectCategory();
				}
				chosenCategories[playerTurn][catNum] = true;
				int catScore = calculateCategoryScore(catNum, dice);
				
				updateGameScores(catNum, playerTurn, catScore);
			}
		}
		calculateBonuses();
		int winner = determineGameWinner();
		display.printMessage("Congratulations, " + playerNames[winner - 1] + ", you're the winner with a total score of " + playerScores[winner] + "!");
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[] playerScores = new int[5];
	private int[] upperScores = new int[5];
	private int[] lowerScores = new int[5];
	private Boolean[][] chosenCategories = new Boolean[5][18];
	
	// Returns random dice array with a size of 5
	private int[] getRandomDice(){
		int[] dice = new int[5];
		for (int i = 0; i < 5; i++) {
			dice[i] = rgen.nextInt(1,6);
		}
		return dice;
	}
	
	// Returns score of chosen category with current dice
	private int calculateCategoryScore(int catNum, int[] dice) {
		int score = 0;
		
		if (catNum >= ONES && catNum <= SIXES) {
			for (int i = 0; i < 5; i++) {
				if (dice[i] == catNum)
					score += catNum;
			}
		}
		if (categoryChecker(catNum, dice)) {
			if(catNum == THREE_OF_A_KIND || catNum == FOUR_OF_A_KIND) {
				for (int i =0; i < 5; i++) {
					score += dice[i];
				}
			}
			
			if (catNum == FULL_HOUSE) {
				score = 25;
			}
			
			if (catNum == SMALL_STRAIGHT) {
				score = 30;
			}
			
			if (catNum == LARGE_STRAIGHT) {
				score = 40;
			}
			
			if (catNum == YAHTZEE) {
				score = 50;
			}
			
			if (catNum == CHANCE) {
				for (int i = 0; i < 5; i++) {
					score += dice[i];
				}
			}
		}
		
		
		return score;
	}
	
	// Updates the scoreboard with current turn scores
	private void updateGameScores(int catNum, int playerTurn, int catScore) {
		playerScores[playerTurn] += catScore;
		display.updateScorecard(17, playerTurn, playerScores[playerTurn]);
		if (catNum <= 6) {
			upperScores[playerTurn] += catScore;
			display.updateScorecard(7, playerTurn, upperScores[playerTurn]);
		}
		else {
			lowerScores[playerTurn] += catScore;
			display.updateScorecard(16, playerTurn, lowerScores[playerTurn]);
		}
		display.updateScorecard(catNum, playerTurn, catScore);
	}
	
	// Randomizes only selected dice and returns dice array
	private int[] randomizeSelectedDice(int[] prevDice) {
		int[] dice = new int[5];
		for (int i = 0; i < 5; i++) {
			if (display.isDieSelected(i)) {
				dice[i] = rgen.nextInt(1,6);
			}
			else {
				dice[i] = prevDice[i];
			}
		}
		return dice;
	}
	
	// Calculates bonuses at the end of the game
	private void calculateBonuses() {
		for (int i = 1; i <= nPlayers; i++) {
			if (upperScores[i] > 67){
				playerScores[i] += 35;
				display.updateScorecard(UPPER_BONUS, i, 35);
			}
			else {
				display.updateScorecard(UPPER_BONUS, i, 0);
			}
		}
	}
	
	// Checks who won the game and returns their player ID
	private int determineGameWinner() {
		int maxScore = -1;
		int maxScorePlayer = 1;
		for (int i = 1; i <= nPlayers; i++) {
			if (playerScores[i] > maxScore) {
				maxScorePlayer = i;
				maxScore = playerScores[i];
			}
		}
		return maxScorePlayer;
	}
	
	// Checks if the given dice pattern satisfies the category
	private Boolean categoryChecker(int catNum, int[] dice) {
		if (catNum >= ONES && catNum <= SIXES) {
			return true;
		}
		if (catNum == THREE_OF_A_KIND) {
			for (int i = 0; i < 5; i++) {
				int counter = 0;
				for (int j = 0; j < 5; j++) {
					if (dice[i] == dice[j])
						counter++;
					if (counter >= 3)
						return true;
				}
			}
		}
		if (catNum == FOUR_OF_A_KIND) {
			for (int i = 0; i < 5; i++) {
				int counter = 0;
				for (int j = 0; j < 5; j++) {
					if (dice[i] == dice[j])
						counter++;
					if (counter >= 4)
						return true;
				}
			}
		}
		if (catNum == FULL_HOUSE) {
			boolean pair = false;
			boolean triple = false;
			for (int i = 0; i < 5; i++) {
				int counter = 0;
				for (int j = 0; j < 5; j++) {
					if (dice[i] == dice[j])
						counter++;
				}
				if (counter == 2)
					pair = true;
				if (counter == 3)
					triple = true;
			}
			if (pair && triple) {
				return true;
			}
		}
		if (catNum == SMALL_STRAIGHT) {
			int[] sortedDice = dice;
			Arrays.sort(sortedDice);
			int counter = 0;
			for (int i = 1; i < 5; i++) {
				if (dice[i] - 1 == dice[i-1])
					counter++;
				else
					counter = 1;
				
				if (counter == 4)
					return true;
			}
		}
		if (catNum == LARGE_STRAIGHT) {
			int[] sortedDice = sortDiceArray(dice);
			for (int i = 1; i < 5; i++) {
				if (sortedDice[i] - 1 != sortedDice[i-1])
					return false;
			}
				return true;
		}
		if (catNum == YAHTZEE) {
			for (int i = 1; i < 5; i++) {
				if (dice[0] != dice[i])
					return false;
			}
			return true;
		}
		if (catNum == CHANCE) {
			return true;
		}
		
		return false;
	}
	
	// Sorts the dice array in increasing way and returns sorted dice array
	private int[] sortDiceArray(int[] dice) {
		int[] sortedDice = dice;
		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 5; j++) {
				if (sortedDice[j-1] > sortedDice[j]) {
					int temp = sortedDice[j-1];
					sortedDice[j-1] = sortedDice[j];
					sortedDice[j] = temp;
				}
			}
		}
		return sortedDice;
	}
	
	
}
