/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
 
#include "pqueue-heap.h"
#include "error.h"

HeapPriorityQueue::HeapPriorityQueue() {
	length = 0;
	maxLength = 50;
	arr = new string[maxLength];
}

HeapPriorityQueue::~HeapPriorityQueue() {
	delete[] arr;
}

int HeapPriorityQueue::size() {
	return length;
}

bool HeapPriorityQueue::isEmpty() {
	return length == 0;
}

void HeapPriorityQueue::enqueue(string value) {
	length++;
	if (length == maxLength) {
		addLength();
	}
	arr[length] = value;
	bubbleUp(length);
}

string HeapPriorityQueue::peek() {
	if (length == 0) {
		error("length is 0");
	}
	return arr[1];
}

string HeapPriorityQueue::dequeueMin() {
	string val = peek();
	arr[1] = arr[length--];
	bubbleDown(1);
	
	return val;
}

void HeapPriorityQueue::bubbleUp(int i) {
	if (i <= 1) 
		return;
	int PARENT = parent(i);
	if (arr[PARENT] > arr[i]) {
		string temp = arr[i];
		arr[i] = arr[PARENT];
		arr[PARENT] = temp;
		bubbleUp(PARENT);
	}
}

void HeapPriorityQueue::bubbleDown(int i) {
	int LEFT = left(i);
	int RIGHT = right(i);
	
	if (LEFT <= length && RIGHT <= length) {
		int MIN = -1;
		if (arr[LEFT] < arr[RIGHT]) {
			MIN = LEFT;
		}
		else {
			MIN = RIGHT;
		}

		if (MIN != -1 && arr[MIN] < arr[i]) {
			string temp = arr[MIN];
			arr[MIN] = arr[i];
			arr[i] = temp;
			bubbleDown(MIN);
		}
	}
	else
	if (LEFT <= length) {
		if (arr[LEFT] < arr[i]) {
			string temp = arr[LEFT];
			arr[LEFT] = arr[i];
			arr[i] = temp;
		}
	}
}
int HeapPriorityQueue::left(int i) {
	return i * 2;
}
int HeapPriorityQueue::right(int i) {
	return i * 2 + 1;
}
int HeapPriorityQueue::parent(int i) {
	return i / 2;
}
void HeapPriorityQueue::addLength() {
	maxLength *= 2;
	string* oldArr = arr;
	arr = new string[maxLength];

	for (int i = 1; i < length; i++) {
		arr[i] = oldArr[i];
	}
	delete[] oldArr;
}