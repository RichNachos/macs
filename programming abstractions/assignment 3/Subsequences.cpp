/*
 * File: Subsequences.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Subsequences problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given two strings, returns whether the second string is a
 * subsequence of the first string.
 */
bool isSubsequence(string text, string subsequence) {
	
	if (text.length() == 0 && subsequence.length() > 0) {
		return false;
	}
	if (subsequence.length() == 0) {
		return true;
	}

	if (text[0] != subsequence[0]) {
		return isSubsequence(text.substr(1,text.length()),subsequence);
	}
	else {
		return isSubsequence(text.substr(1,text.length()),subsequence.substr(1,subsequence.length()));
	}
}

int main() {
	string text = getLine("Enter string: ");
	string subsequence = getLine("Enter subsequence: ");

	if (isSubsequence(text,subsequence)) {
		cout << "Is subsequence" << endl;
	}
	else {
		cout << "Is not subsequence" << endl;
	}
    return 0;
}
