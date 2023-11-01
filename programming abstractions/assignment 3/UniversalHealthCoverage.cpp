/*
 * File: UniversalHealthCoverage.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the UniversalHealthCoverage problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include <string>
#include "set.h"
#include "vector.h"
#include "console.h"
#include "foreach.h"
using namespace std;

/* Function: canOfferUniversalCoverage(Set<string>& cities,
 *                                     Vector< Set<string> >& locations,
 *                                     int numHospitals,
 *                                     Vector< Set<string> >& result);
 * Usage: if (canOfferUniversalCoverage(cities, locations, 4, result)
 * ==================================================================
 * Given a set of cities, a list of what cities various hospitals can
 * cover, and a number of hospitals, returns whether or not it's
 * possible to provide coverage to all cities with the given number of
 * hospitals.  If so, one specific way to do this is handed back in the
 * result parameter.
 */
bool canOfferUniversalCoverage(Set<string>& cities, Vector< Set<string> >& locations, int numHospitals, Vector< Set<string> >& result) {
	if (cities.size() == 0) return true; // If no more cities left to cover, return true
	if (numHospitals == 0 && cities.size() > 0) return false; // If there are cities to cover and no more hospitals to build, return false
	
	for (int i = 0; i < locations.size(); i++) {
		Set<string> hospitalLoc = locations[i]; // Choose a hospital

		// Declare location of hospitals without the current chosen hospital
		Vector< Set<string> > nextLocations = locations;
		nextLocations.remove(i); 

		// Declare uncoveredCities variable which represents the set of all cities which are uncovered by the chosen hospital
		Set<string> uncoveredCities = cities;
		foreach (string city in hospitalLoc) {
			uncoveredCities.remove(city);
		}
	
		// Recursion
		if (canOfferUniversalCoverage(uncoveredCities, nextLocations, numHospitals - 1, result)) {
			result.add(hospitalLoc);
			return true;
		}
	}

	return false;
}


int main() {
	// Test case
    string a = "a", b = "b", c = "c", d = "d", e = "e", f = "f";
    
	Set<string> cities;
    cities += a,b,c,d,e,f;
    Vector< Set<string> > locations;
    
    Set<string> hos1;
    hos1 += a,b,c;
    Set<string> hos2;
    hos2 += a,c,d;
    Set<string> hos3;
    hos3 += b,f;
    Set<string> hos4;
    hos4 += c,e,f;
    
    locations += hos1, hos2, hos3, hos4;
    int numHospitals = 3; // Number of hospitals possible to build
    Vector< Set<string> > result;
    
    if(canOfferUniversalCoverage(cities, locations, numHospitals, result)){
        cout<<"Universal Healthcare is possible!"<<endl;
		cout<<result.toString()<<endl;
        return 0;
    } else {
        cout<<"Universal Healthcare is not possible!"<<endl;
        return 0;
    }
}
