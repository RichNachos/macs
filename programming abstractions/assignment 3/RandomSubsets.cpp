/*
 * File: RandomSubsets.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Random Subsets problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "set.h"
#include "random.h"
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given a set of integers, returns a uniformly-random subset of that
 * set.
 */
Set<int> randomSubsetOf(Set<int>& s) {
	if (s.size() == 0) {
		return s;
	}
	bool includeInt = randomInteger(0,1);

	Set<int> subset = s;
	subset.remove(subset.first());
	subset = randomSubsetOf(subset);

	if (includeInt) {
		subset.add(s.first());
	}
	return subset;
}

int main() {
	int n = getInteger("Enter amount of integers: ");
	Set<int> set;


	for (int i = 0; i < n; i++) {
		int k = getInteger("Enter integer: ");
		set.add(k);
	}

	
	Set<int> randomSubset = randomSubsetOf(set);
	foreach (int i in randomSubset) {
		cout << i << " ";
	}
	cout << endl;

    return 0;
}
