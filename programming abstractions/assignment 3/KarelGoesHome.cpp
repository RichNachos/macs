/*
 * File: KarelGoesHome.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Karel Goes Home
 * warmup problem for Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given a street number and avenue number, which are 1-indexed,
 * returns the number of paths Karel can take back home that only
 * moves left or down.
 */
int numPathsHome(int street, int avenue) { 
	if (street == 1 || avenue == 1) {
		return 1;
	}
	int sum = 0;
	if (street != 1) {
		sum += numPathsHome(street - 1, avenue);
	}
	if (street != 1) {
		sum += numPathsHome(street, avenue - 1);
	}
	return sum;
}

int main() {
	int street = getInteger("Enter Street Number: ");
	int avenue = getInteger("Enter Avenue Number: ");

	int pathsHome = numPathsHome(street, avenue);
	cout << "Paths home is: " << pathsHome <<endl;
    return 0;
}
