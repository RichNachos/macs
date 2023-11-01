/*
 * File: Sierpinski.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Sierpinski problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <cmath>
#include "gwindow.h"
#include "simpio.h"
using namespace std;

const int windowHeight = 800;
const int windowWidth = 800;

void drawTriangle(GWindow w, GPoint p, double length) {
	p = w.drawPolarLine(p, length, 0);
	p = w.drawPolarLine(p, length, 120);
	p = w.drawPolarLine(p, length, -120);
}

void drawSierpinski(GWindow w, GPoint p, double length, int order) {
	drawTriangle (w, p, length);

	if (order == 0) {
		return;
	}

	drawSierpinski(w, p, length / 2, order - 1); // First triangle
	
	GPoint p1 = w.drawPolarLine(p, length / 2, 0);
	drawSierpinski(w, p1, length / 2, order - 1); // Second triangle

	GPoint p2 = w.drawPolarLine(p, length / 2, 60);
	drawSierpinski(w, p2, length / 2, order - 1); // Third triangle
}

int main() {
	int length = getInteger("Enter length: ");
	int order = getInteger("Enter Sierpinsi order: ");
	
	GWindow w(windowWidth, windowHeight);
	GPoint startingPoint( (w.getWidth() / 2) - (length / 2), (w.getHeight() / 2) + (sqrt(3.0) * length / 6) ); // Starting point of triangle directly in the middle of the window
	
	drawSierpinski(w, startingPoint, length, order); // Starting Sierpinski triangle

    return 0;
}
