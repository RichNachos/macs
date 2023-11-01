/*
 * File: RandomWriter.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Random Writer problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <fstream>
#include "map.h"
#include "vector.h"
#include "console.h"
#include "random.h"
#include "simpio.h"
using namespace std;

string generateText(Map<string,Vector<char> >& analysis, int markovOrder, int symbolsToGenerate);
Map<string,Vector<char> > analyzeFile(ifstream& reader, int markovOrder);

Vector<char> stringToVector(string s) {
	Vector<char> v;
	for (int i = 0; i < s.size(); i++) {
		v.add(s[i]);
	}
	return v;
}

Vector<char> findMostFrequentSequence(Map<string,Vector<char> > map) {
	Vector<char> sequence;
	Vector<char> longVec;
	foreach (string key in map) {
		Vector<char> vec = map.get(key);
		if (vec.size() > longVec.size()) {
			longVec = vec;
			sequence = stringToVector(key);
		}
	}
	return sequence;
}


string vectorToString(Vector<char> v) {
	string result;
	foreach (char c in v) {
		result += c;
	}
	return result;
} 

int main() {
    int markovOrder;
	int symbolsToGenerate = 2000;

    string file;
	file = getLine("Enter file name: ");
	
	ifstream reader;
	reader.open(file.c_str());
	while (!reader.is_open()) {
		file = getLine("Couldn't open file, enter new file: ");
		
		reader.clear();
		reader.open(file.c_str());
	}

	markovOrder = getInteger("Enter Markov order (1-10): ");
	while (markovOrder < 1 || markovOrder > 10) {
		markovOrder = getInteger("Invalid Markov order integer, enter new one: (1-10): ");
	}
	
	cout<<"Processing..."<<endl;
	Map<string,Vector<char> > analysis = analyzeFile(reader,markovOrder);
	string output = generateText(analysis, markovOrder, symbolsToGenerate);
	
	cout<<output<<endl;
    return 0;
}

string generateText(Map<string,Vector<char> >& analysis, int markovOrder, int symbolsToGenerate) {
	
	Vector<char> lastSequence = findMostFrequentSequence(analysis);
	string output = vectorToString(lastSequence);
	for (int i = markovOrder; i <= symbolsToGenerate; i++) {
		
		Vector<char> freqVec = analysis.get(vectorToString(lastSequence));
		int randomInt = randomInteger(0, freqVec.size() - 1);
		
		if (freqVec.size() == 0) 
			break;

		char c = freqVec[randomInt];
		
		lastSequence.add(c);
		lastSequence.remove(0);

		output += c;
	}
	return output;
}

Map<string,Vector<char> > analyzeFile(ifstream& reader, int markovOrder) {
	Map<string,Vector<char> > analysis;
	char c;
	Vector<char> key;
	while (true) {
		c = reader.get();
		if (!reader) {
			break;
		}
		Vector<char> vec = analysis.get(vectorToString(key));
		vec.add(c);
		if (key.size() == markovOrder) {
			analysis.put(vectorToString(key),vec);
		}
		key.add(c);
		if (key.size() > markovOrder) {
			key.remove(0);
		}
	}
	return analysis;
}
