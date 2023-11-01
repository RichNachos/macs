/*
 * File: FleschKincaid.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Flesch-Kincaid problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <fstream>
#include "console.h"
#include "tokenscanner.h"
#include "ctype.h"
using namespace std;

bool isVowel(char c) {
	c = tolower(c);
	if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y') {
		return true;
	}
	return false;
}
int countSyllables(string word) {
	int count = 0;
	bool prevIsVowel = false;
	for (int i = 0; i < word.length(); i++) {
		if (isVowel(word[i])) {
			count++;
			if ((tolower(word[i]) == 'e' && i == word.length() - 1) || prevIsVowel) {
				count--;
			}
			prevIsVowel = true;
		}
		else {
			prevIsVowel = false;
		}
	}
	if (count == 0)
		return 1;
	return count;
}
bool containsSentenceEnder(string word) {
	
	for (int i = 0; i < word.length(); i++) {
		if (word[i] == '.' || word[i] == '!' || word[i] == '?') {
			return true;
		}
	}
	return false;
}
int main() {
	cout<<"Enter file name: ";
    string file;
	cin>>file;
	
	ifstream reader;
	reader.open(file.c_str());
	while (!reader.is_open()) {
		cout<<"Couldn't open file, enter new file: ";
		cin>>file;
		
		reader.clear();
		reader.open(file.c_str());
	}

	int sentenceCounter = 0;
	int wordCounter = 0;
	int syllableCounter = 0;

	double C0 = -15.59;
	double C1 = 0.39;
	double C2 = 11.8;

	TokenScanner scanner(reader);
	scanner.ignoreWhitespace();
	scanner.addWordCharacters("'");

    while (scanner.hasMoreTokens()) {
		string word = scanner.nextToken();
		
		if (isalpha(word[0])) {
			wordCounter++;
			syllableCounter += countSyllables(word);
		}
		if (containsSentenceEnder(word)) {
			sentenceCounter++;
		}
    }
	reader.close();
	double grade = C0 + (C1 * (double(wordCounter) / sentenceCounter)) + (C2 * (double(syllableCounter) / wordCounter));

	cout<<"Words: "<<wordCounter<<endl;
	cout<<"Syllables: "<<syllableCounter<<endl;
	cout<<"Sentences: "<<sentenceCounter<<endl;
	cout<<"Grade level: "<<grade<<endl;
    

	return 0;
}
