/*
 * File: Combinations.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Combinations problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
using namespace std;

int calculatePascalNumber(int r, int i) {
	if (i < 0){
		return 0;
	}
	if (i == 0 || r == i){
		return 1;
	}
	return calculatePascalNumber(r - 1, i - 1) + calculatePascalNumber(r - 1, i);
}

int main() {
    int row,index;
	cout<<"Enter row number: ";
	cin>>row;

	cout<<"Enter index number: ";
	cin>>index;

	cout<<calculatePascalNumber(row,index)<<endl;
    return 0;
}
