/*
 * File: WordLadder.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Word Ladder problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "vector.h"
#include "queue.h"
#include "lexicon.h"
#include "set.h"
using namespace std;

Vector<string> offByOneWords(string startWord, Lexicon allWords) {
	Vector<string> foundWords;
	for (int i = 0; i < startWord.length(); i++) {
		string changedWord = startWord;
		for (char j = 'a'; j <= 'z'; j++) {
			changedWord[i] = j;
			if (allWords.contains(changedWord)) {
				foundWords.add(changedWord);
			}
		}
	}
	return foundWords;
}

bool offByOneChar(string firstWord, string secondWord) {
	if (firstWord.length() != secondWord.length())
		return false;

	int offCount = 0;
	for (int i = 0; i < firstWord.length(); i++) {
		if (firstWord[i] != secondWord[i])
			offCount++;
	}
	if (offCount == 1)
		return true;
	return false;
}

bool ladderContainsWord(Vector<string> ladder, string word) {
	for (int i = 0; i < ladder.size() - 1; i++) {
		if (ladder[i] == word)
			return true;
	}
	return false;
}
void displayLadder(Vector<string> ladder) {
	for (int i = 0; i < ladder.size() - 1; i++) {
		cout<<ladder[i]<<" -> ";
	}
	cout<<ladder[ladder.size() - 1]<<endl;
}

int main() {
	Lexicon allWords("EnglishWords.dat");

	while (true) {
		string startWord;
		string endWord;

		cout<<"Enter start word (RETURN to exit): ";
		cin>>startWord;
		cout<<"Enter destination word: ";
		cin>>endWord;
	
		// Queue initialization
		Queue<Vector<string> > ladderQueue;
		Vector<string> baseVector;
		baseVector.add(startWord);
		ladderQueue.enqueue(baseVector);
	
		// Final Ladder vector init
		Vector<string> shortestLadder;
		cout<<"Searching..."<<endl;
		
		if (startWord.length() != endWord.length()) {
			cout<<"No word ladder could be found"<<endl;
			continue;
		}

		if (startWord == endWord) {
			cout<<"Ladder found: "<<startWord<<endl;
			continue;
		}

		Lexicon usedWords;
		usedWords.add(startWord);
		while (true) {
			Vector<string> currentLadder = ladderQueue.dequeue();
			string currentLastWord = currentLadder[currentLadder.size() - 1];
			
			if (currentLastWord == endWord) {
				shortestLadder = currentLadder;
				break;
			}
			Vector<string> offByOneWordsVector = offByOneWords(currentLastWord, allWords);
	
			
			foreach(string newWord in offByOneWordsVector) {
				if (offByOneChar(currentLastWord, newWord) &&  !usedWords.contains(newWord)) {
					usedWords.add(newWord);
					Vector<string> possibleLadder = currentLadder;
					possibleLadder.add(newWord);
					ladderQueue.enqueue(possibleLadder);
				}
			}
		}
		cout<<"Ladder found: ";
		displayLadder(shortestLadder);
	}
    return 0;
}
