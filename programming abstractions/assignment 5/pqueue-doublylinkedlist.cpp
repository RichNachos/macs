/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-doublylinkedlist.h"
#include "error.h"

DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	length = 0;
	head = NULL;
}

DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
	while (head != NULL) {
		cell* next = head->next;
		delete head;
		head = next;
	}
}

int DoublyLinkedListPriorityQueue::size() {
	return length;
}

bool DoublyLinkedListPriorityQueue::isEmpty() {	
	return length == 0;
}

void DoublyLinkedListPriorityQueue::enqueue(string value) {
	cell* newCell = new cell;
	newCell->prev = NULL;
	newCell->value = value;
	if (head != NULL) {
		newCell->next = head;
		head->prev = newCell;
		head = newCell;
	}
	else {
		newCell->next = NULL;
		head = newCell;
	}
	length++;
}

string DoublyLinkedListPriorityQueue::peek() {
	if (length == 0) {
		error("length is 0");
		return 0;
	}
	
	string first = head->value;
	cell* ptr = head;
	while (ptr != NULL) {
		if (ptr->value < first) {
			first = ptr->value;
		}
		ptr = ptr->next;
	}
	return first;
}

string DoublyLinkedListPriorityQueue::dequeueMin() {
	if (length == 0) {
		error("length is 0");
		return 0;
	}
	
	cell* first = head;
	cell* ptr = head;
	while (ptr != NULL) {
		if (ptr->value < first->value) {
			first = ptr;
		}
		ptr = ptr->next;
	}
	
	if (first->prev != NULL) {
		first->prev->next = first->next;
	}
	else {
		head = first->next;
	}
	if (first->next != NULL) {
		first->next->prev = first->prev;
	}

	string result = first->value;
	length--;
	delete first;
	return result;
}

