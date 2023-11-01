/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gboggle.h"
#include "grid.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include "strlib.h"
#include "foreach.h"
using namespace std;

/* Constants */

const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;

const string STANDARD_CUBES[16]  = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* Function prototypes */

void welcome();
void giveInstructions();
Grid<string> boggleConfiguration();
bool chooseBoggleGrid();
Grid<string> chooseBoggleConfiguration(bool isGrid4x4);
Grid<string> drawDefaultBoggle(bool isGrid4x4);
Grid<string> drawCustomBoggle(string conf, bool isGrid4x4);
bool startGame(Grid<string> board);
bool isValidWord(string word, Grid<string> board);
bool isValidSequence(string, Grid<string> board, Grid<bool>& walkedPath, int row, int col);
void highlightPath(Grid<bool> walkedPath);
void findAllWords(Grid<string> board, Lexicon& usedWords);
void findAllPossibleWords(string sequence, Grid<string> board, Lexicon& usedWords, Grid<bool> walkedPath, int row, int col, Lexicon& englishWords);
/* Main program */

int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    initGBoggle(gw);
    welcome();
    giveInstructions();
	
	bool continueGame = true;
	while (continueGame) {
		Grid<string> board = boggleConfiguration();
		continueGame = startGame(board);
		cout<<endl;
		initGBoggle(gw);
	}

    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready...";
    getLine();
}

// Everything to do with the configuration of Boggle board
Grid<string> boggleConfiguration() {
	bool isGrid4x4 = chooseBoggleGrid();
	Grid<string> board = chooseBoggleConfiguration(isGrid4x4);
	return board;
}

// Lets player choose 4x4 or 5x5 Boggle board
bool chooseBoggleGrid() {
	cout<<"You can choose your Boggle grid to be 4x4 or 5x5"<<endl;
	string answer = getLine("Do you want your Boggle grid to be 4x4? (Y/N): ");

	if (toUpperCase(answer)[0] == 'Y') {
		cout<<"You chose 4x4 Boggle grid"<<endl;
		drawBoard(4,4);
		return true;
	}
	else {
		cout<<"You chose 5x5 Boggle grid"<<endl;
		drawBoard(5,5);
		return false;
	}
}

// Lets the user choose if he wants to force the board configuration
Grid<string> chooseBoggleConfiguration(bool isGrid4x4) {
	cout<<"You can choose boggle letter configuration if you want to"<<endl;
	string answer = getLine("Do you want to force Boggle letter configuration? (Y/N): ");
	string conf = "";
	
	if (toUpperCase(answer)[0] == 'Y') {
		if (isGrid4x4) {
			while (conf.length() < 16) {
				conf = getLine("Enter 16 letters as a string sequence: ");
			}
		}
		else {
			while (conf.length() < 25) {
				conf = getLine("Enter 25 letters as a string sequence: ");
			}
		}
	}
	Grid<string> board;
	if (conf.length() == 0) {
		board = drawDefaultBoggle(isGrid4x4);
	}
	else {
		board = drawCustomBoggle(conf, isGrid4x4);
	}
	return board;
}

// Labels the cube  faces with random letters given by a global constant variables
Grid<string> drawDefaultBoggle(bool isGrid4x4) {
	
	if (isGrid4x4) {
		Grid<string> board(4,4);
		for (int i = 0; i < 16; i++) {
			int row = i / 4;
			int col = i % 4;

			int cubeFace = randomInteger(0,5);
			char letter = STANDARD_CUBES[i][cubeFace];
			board[row][col] = letter;
			labelCube(row, col, letter);
		}
		return board;
	}
	else {
		Grid<string> board(5,5);
		for (int i = 0; i < 25; i++) {
			int row = i / 5;
			int col = i % 5;

			int cubeFace = randomInteger(0,5);
			char letter = BIG_BOGGLE_CUBES[i][cubeFace];
			board[row][col] = letter;
			labelCube(row, col, letter);
		}
		return board;
	}
	
}

// Labels cube faces with the string given by the user
Grid<string> drawCustomBoggle(string conf, bool isGrid4x4) {

	conf = toUpperCase(conf);
	if (isGrid4x4) {
		Grid<string> board(4,4);
		for (int i = 0; i < 16; i++) {
			int row = i / 4;
			int col = i % 4;
			char letter = conf[i];
			board[row][col] = letter;
			labelCube(row, col, letter);
		}
		return board;
	}
	else {
		Grid<string> board(5,5);
		for (int i = 0; i < 25; i++) {
			int row = i / 5;
			int col = i % 5;
			char letter = conf[i];
			board[row][col] = letter;
			labelCube(row, col, letter);
		}
		return board;
	}
}

// Starts the game, lets the user continue to enter words
bool startGame(Grid<string> board) {
	Lexicon englishWords("EnglishWords.dat");
	Lexicon usedWords;

	// Player's turn
	while (true) {
		string word = getLine("Enter a word or enter nothing to end your turn: ");
		if (word == "") {
			break;
		}
		word = toUpperCase(word);
		
		if (englishWords.contains(word)) {
			if (!usedWords.contains(word)) {
				if (isValidWord(word, board)) {
					usedWords.add(word);
					cout<<"Word found!"<<endl;
					recordWordForPlayer(word, HUMAN);
				}
				else {
					cout<<"No word found on board"<<endl;
				}
			}
			else {
				cout<<"Already used that word"<<endl;
			}
		}
		else {
			cout<<"That word doesn't exist in the English language"<<endl;
		}
	}

	// Computer's turn
	findAllWords(board, usedWords);
	
	// Asking the player if he wants to play game again
	string answer = getLine("Do you wish to play again? (Y/N): ");
	if (toUpperCase(answer)[0] == 'Y') { 
		return true;
	}
	else {
		return false;
	}
}

// Checks if the given word is found on board
bool isValidWord(string word, Grid<string> board) {
	if (word.length() < 4) {
		return false;
	}
	
	for (int i = 0; i < board.nRows; i++) {
		for (int j = 0; j < board.nCols; j++) {
			Grid<bool> walkedPath(board.nRows, board.nCols);
			if (isValidSequence(word, board, walkedPath, i, j)) {
				highlightPath(walkedPath);
				return true;
			}
		}
	}
	return false;
}

// Checks if a given sequence of string is a word that is found on board
bool isValidSequence(string sequence, Grid<string> board, Grid<bool>& walkedPath, int row, int col) {
	if (sequence.length() == 0) {
		return true;
	}
	if (board[row][col][0] != sequence[0] || walkedPath[row][col] == true) {
		return false;
	}
	int dr [8] = {-1, -1, -1, 0, 0, 1, 1, 1};
	int dc [8] = {-1, 0, 1, -1, 1, -1, 0, 1};
	walkedPath[row][col] = true;

	for (int i = 0; i < 8; i++) {
		string nextSequence = sequence.substr(1, sequence.length() - 1);
		Grid<bool> nextWalkedPath = walkedPath;
		if (row + dr[i] >= 0 && row + dr[i] < board.nRows && col + dc[i] >= 0 && col + dc[i] < board.nCols) {
			if (isValidSequence(nextSequence, board, nextWalkedPath, row + dr[i], col + dc[i])) {
				walkedPath = nextWalkedPath;
				return true;
			}
		}
	}
	return false;
}

// Highlights the path the program took while finding a certain word
void highlightPath(Grid<bool> walkedPath) {
	for (int i = 0; i < walkedPath.nRows; i++) {
		for (int j = 0; j < walkedPath.nCols; j++) {
			highlightCube(i, j, walkedPath[i][j]);
		}
	}
	pause(800);
	for (int i = 0; i < walkedPath.nRows; i++) {
		for (int j = 0; j < walkedPath.nCols; j++) {
			highlightCube(i, j, false);
		}
	}
}

// Supplement for recursive function
void findAllWords(Grid<string> board, Lexicon& usedWords) {
	Grid<bool> walkedPath(board.nRows, board.nCols);
	Lexicon englishWords("EnglishWords.dat");
	for (int i = 0; i < board.nRows; i++) {
		for (int j = 0; j < board.nCols; j++) {
			findAllPossibleWords("", board, usedWords, walkedPath, i, j, englishWords);
		}
	}
}

// Recursively finds all words on the board
void findAllPossibleWords(string sequence, Grid<string> board, Lexicon& usedWords, Grid<bool> walkedPath, int row, int col, Lexicon& englishWords) {
	if (walkedPath[row][col]) {
		return;
	}
	
	sequence += board[row][col];
	walkedPath[row][col] = true;

	if (!englishWords.containsPrefix(sequence)) {
		return;
	}
	if (englishWords.contains(sequence) && !usedWords.contains(sequence) && sequence.length() >= 4) {
		usedWords.add(sequence);
		recordWordForPlayer(sequence, COMPUTER);
	}

	int dr [8] = {-1, -1, -1, 0, 0, 1, 1, 1};
	int dc [8] = {-1, 0, 1, -1, 1, -1, 0, 1};
	
	for (int i = 0; i < 8; i++) {
		if (row + dr[i] >= 0 && row + dr[i] < board.nRows && col + dc[i] >= 0 && col + dc[i] < board.nCols) {
			findAllPossibleWords(sequence, board, usedWords, walkedPath, row + dr[i], col + dc[i], englishWords);
		}
	}
}
