/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-linkedlist.h"
#include "error.h"
#include "console.h"

LinkedListPriorityQueue::LinkedListPriorityQueue() {
	head = NULL;
	length = 0;
}

LinkedListPriorityQueue::~LinkedListPriorityQueue() {
	while (head != NULL) {
		cell* ptr = head->nextCell;
		delete head;
		head = ptr;
	}
}

int LinkedListPriorityQueue::size() {
	return length;
}

bool LinkedListPriorityQueue::isEmpty() {
	return length == 0;
}

void LinkedListPriorityQueue::enqueue(string value) {
	if (head == NULL) {
		head = new cell;
		head->value = value;
		head->nextCell = NULL;
		length++;
		return;
	}

	cell* ptr = head;
	cell* prev;
	while (ptr != NULL) {
		if (ptr->value > value) {
			cell* nextPtr = new cell;
			nextPtr->value = ptr->value;
			nextPtr->nextCell = ptr->nextCell;
			ptr->value = value;
			ptr->nextCell = nextPtr;
			length++;
			return;
		}
		prev = ptr;
		ptr = ptr->nextCell;
	}
	ptr = new cell;
	ptr->value = value;
	ptr->nextCell = NULL;
	prev->nextCell = ptr;
	length++;
}

string LinkedListPriorityQueue::peek() {
	if (head != NULL)
		return head->value;
	else
		error("length is 0");
}

string LinkedListPriorityQueue::dequeueMin() {
	if (length == 0) {
		error("length is 0");
		return NULL;
	}

	string val = head->value;
	cell* ptr = head;
	head = head->nextCell;
	length--;
	delete ptr;
	return val;
}
