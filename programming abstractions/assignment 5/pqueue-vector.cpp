/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
 
#include "pqueue-vector.h"
#include "error.h"



VectorPriorityQueue::VectorPriorityQueue() {
	// TODO: Fill this in!
}

VectorPriorityQueue::~VectorPriorityQueue() {
	// TODO: Fill this in!
}

int VectorPriorityQueue::size() {
	// TODO: Fill this in!
	
	return pQueue.size();
}

bool VectorPriorityQueue::isEmpty() {
	// TODO: Fill this in!
	
	return pQueue.isEmpty();
}

void VectorPriorityQueue::enqueue(string value) {
	pQueue.add(value);
}

string VectorPriorityQueue::peek() {
	string minVal = pQueue[0];
	for (int i = 0; i < pQueue.size(); i++) {
		if (pQueue[i] < minVal) {
			minVal = pQueue[i];
		}
	}
	return minVal;
}

string VectorPriorityQueue::dequeueMin() {
	string minVal = pQueue[0];
	int index = 0;
	for (int i = 0; i < pQueue.size(); i++) {
		if (pQueue[i] < minVal) {
			minVal = pQueue[i];
			index = i;
		}
	}
	pQueue.remove(index);
	return minVal;
}

