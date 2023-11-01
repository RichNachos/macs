/******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
#include <algorithm>
#include "random.h"
#include "foreach.h"
#include <limits>
using namespace std;

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */
Vector<Loc>
shortestPath(Loc start,
             Loc end,
             Grid<double>& world,
             double costFn(Loc from, Loc to, Grid<double>& world),
			 double heuristic(Loc start, Loc end, Grid<double>& world)) {
	
	Set<Loc> green; // Green locations
	Set<Loc> yellow; // Yellow locations
	Map<Loc, double> costs; // Minimum cost to each location
	Map<Loc, Loc> previousLocs; // Previous location to each written location which is the shortest path
	TrailblazerPQueue<Loc> pq; // Priority queue for bfs
	Map<Loc, double> heuristics;

	pq.enqueue(start, heuristic(start, end, world));
	yellow.add(start);
	costs.put(start, 0);
	heuristics.put(start, heuristic(start, end, world));
	previousLocs.put(start, start);

	while (!pq.isEmpty()) {
		Loc curr = pq.dequeueMin();
		green.add(curr);
		yellow.remove(curr);
		colorCell(world, curr, Color::GREEN);
		if (curr == end) {
			break;
		}
		int dr []= {-1, -1, -1, 0, 0, 1, 1, 1};
		int dc []= {-1, 0, 1, -1, 1, -1, 0, 1};
		for (int i = 0; i < 8; i++) {
			Loc v = curr;
			v.row += dr[i];
			v.col += dc[i];

			if (!world.inBounds(v.row, v.col)) {
				continue;
			}

			double newDist = costs.get(curr) + costFn(curr, v, world);
			double newHeur = newDist + heuristic(v, end, world);
			if (yellow.contains(v)) {
				double prevDist = costs.get(v);
				if (newDist < prevDist) {
					costs.put(v, newDist);
					previousLocs.put(v, curr);
					pq.decreaseKey(v, newHeur);
				}
			}
			if (!yellow.contains(v) && !green.contains(v) && newDist != numeric_limits<double>::infinity()) { // newDist must be lower than infinity to ensure that maze walls are not colored yellow and taken into account (slows search by a big margin)
				yellow.add(v);
				colorCell(world, v, Color::YELLOW);
				costs.put(v, newDist);
				previousLocs.put(v, curr);
				pq.enqueue(v, newHeur);
			}
		}
	}
	
	Vector<Loc> calculatedPath; // Final path
	Loc curr = end;
	while (curr != start) {
		calculatedPath.add(curr);
		curr = previousLocs.get(curr);
	}
	calculatedPath.add(start);
	reverse(calculatedPath.begin(), calculatedPath.end());
	
    return calculatedPath;
}


Set<Edge> createMaze(int numRows, int numCols) {
	Vector<Set<Loc> > clusters;
	Set<Edge> edges;
	TrailblazerPQueue<Edge> pq;
	
	for (int i = 0; i < numRows; i++) {
		for (int j = 0; j < numCols - 1; j++) {
			Edge edge;
			Loc loc1;
			Loc loc2;
			loc1.row = i; loc1.col = j;
			loc2.row = i; loc2.col = j + 1;
			edge.start = loc1;
			edge.end = loc2;
			pq.enqueue(edge, randomReal(0, 1));
		}
	}
	for (int i = 0; i < numRows - 1; i++) {
		for (int j = 0; j < numCols; j++) {
			Edge edge;
			Loc loc1;
			Loc loc2;
			loc1.row = i; loc1.col = j;
			loc2.row = i + 1; loc2.col = j;
			edge.start = loc1;
			edge.end = loc2;
			pq.enqueue(edge, randomReal(0, 1));
		}
	}

	for (int i = 0; i < numRows; i++) {
		for (int j = 0; j < numCols; j++) {
			Loc loc;
			loc.row = i;
			loc.col = j;
			Set<Loc> set;
			set.add(loc);
			clusters.add(set);
		}
	}

	while (clusters.size() > 1) {
		Edge edge = pq.dequeueMin();
		bool flag = true;
		int firstIndex;
		int secondIndex;
		for (int i = 0; i < clusters.size(); i++) {
			if (clusters[i].contains(edge.start) && clusters[i].contains(edge.end)) {
				flag = false;
				break;
			}
			if (clusters[i].contains(edge.start)) {
				firstIndex = i;
			}
			if (clusters[i].contains(edge.end)) {
				secondIndex = i;
			}
		}

		if (flag) {
			edges.add(edge);
			Set<Loc> c1 = clusters[firstIndex];
			Set<Loc> c2 = clusters[secondIndex];
			if (secondIndex > firstIndex) {
				clusters.remove(secondIndex);
				clusters.remove(firstIndex);
			}
			else {
				clusters.remove(firstIndex);
				clusters.remove(secondIndex);
			}

			Set<Loc> combinedCluster = c1 + c2;
			//foreach (Loc loc in c2) {
			//	combinedCluster.add(loc);
			//}
			clusters.add(combinedCluster);
		}
	}

    return edges;
}
