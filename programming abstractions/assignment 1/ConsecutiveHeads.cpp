/*
 * File: ConsecutiveHeads.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Consecutive Heads problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
using namespace std;

int main() {
    int counter = 0;
	int headsCounter = 0;
	
	while (headsCounter < 3) {
		if (rand() % 2 == 0){ // heads
			headsCounter++;
			cout<<"Heads"<<endl;
		}
		else { // tails
			headsCounter = 0;
			cout<<"Tails"<<endl;
		}
		counter++;
	}
	cout<<"It took "<<counter<<" flips to get 3 consecutive heads"<<endl;
    return 0;
}
