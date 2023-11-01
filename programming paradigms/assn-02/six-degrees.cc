#include <vector>
#include <list>
#include <set>
#include <string>
#include <iostream>
#include <iomanip>
#include "imdb.h"
#include "path.h"
using namespace std;

bool generateShortestPath(string &source, string &target, imdb &database, path &foundPath);
/**
 * Using the specified prompt, requests that the user supply
 * the name of an actor or actress.  The code returns
 * once the user has supplied a name for which some record within
 * the referenced imdb existsif (or if the user just hits return,
 * which is a signal that the empty string should just be returned.)
 *
 * @param prompt the text that should be used for the meaningful
 *               part of the user prompt.
 * @param db a reference to the imdb which can be used to confirm
 *           that a user's response is a legitimate one.
 * @return the name of the user-supplied actor or actress, or the
 *         empty string.
 */

static string promptForActor(const string& prompt, const imdb& db)
{
  string response;
  while (true) {
    cout << prompt << " [or <enter> to quit]: ";
    getline(cin, response);
    if (response == "") return "";
    vector<film> credits;
    if (db.getCredits(response, credits)) return response;
    cout << "We couldn't find \"" << response << "\" in the movie database. "
	 << "Please try again." << endl;
  }
}

/**
 * Serves as the main entry point for the six-degrees executable.
 * There are no parameters to speak of.
 *
 * @param argc the number of tokens passed to the command line to
 *             invoke this executable.  It's completely ignored
 *             here, because we don't expect any arguments.
 * @param argv the C strings making up the full command line.
 *             We expect argv[0] to be logically equivalent to
 *             "six-degrees" (or whatever absolute path was used to
 *             invoke the program), but otherwise these are ignored
 *             as well.
 * @return 0 if the program ends normally, and undefined otherwise.
 */

int main(int argc, const char *argv[])
{
  imdb db(determinePathToData(argv[1])); // inlined in imdb-utils.h
  if (!db.good()) {
    cout << "Failed to properly initialize the imdb database." << endl;
    cout << "Please check to make sure the source files exist and that you have permission to read them." << endl;
    return 1;
  }
  
  while (true) {
    string source = promptForActor("Actor or actress", db);
    if (source == "") break;
    string target = promptForActor("Another actor or actress", db);
    if (target == "") break;
    if (source == target) {
      cout << "Good one.  This is only interesting if you specify two different people." << endl;
    } else {
      // replace the following line by a call to your generateShortestPath routine... 
      path foundPath(source);
      bool result = generateShortestPath(source, target, db, foundPath);
      
      if (result) {
        cout << foundPath << endl; // Prints the found path
      }
      else {
        cout << endl << "No path between those two people could be found." << endl << endl;
      }
    }
  }
  
  cout << "Thanks for playing!" << endl;
  return 0;
}

bool generateShortestPath(string &source, string &target, imdb &database, path &foundPath) {
  list<path> partialPaths;
  set<string> prevActors;
  set<film> prevFilms;

  partialPaths.push_back(foundPath);
  while (!partialPaths.empty() && partialPaths.front().getLength() <= 5) {
    path pulledPath = partialPaths.front();
    partialPaths.pop_front(); // Pulls and pops the shortest path from queue
    string lastActor = pulledPath.getLastPlayer(); // Saves last added actors name
    vector<film> lastActorFilms; 
    database.getCredits(lastActor, lastActorFilms); // Fills vector<film> with films this actor was in

    for (int i = 0; i < (int)lastActorFilms.size(); i++) { // iterate over all films in which this actor was in
      if (prevFilms.find(lastActorFilms.at(i)) == prevFilms.end()) { // Check if we have seen this film before
        prevFilms.insert(lastActorFilms.at(i)); // Adds current film to seen films
        vector<string> coactors;
        database.getCast(lastActorFilms.at(i), coactors); // Get the actors in the current film  we're looking at
        for (int j = 0; j < (int)coactors.size(); j++) { // Iterate over all the coactors
          if (prevActors.find(coactors.at(j)) == prevActors.end()) {
            prevActors.insert(coactors.at(j)); // Adds actor to previously seen actors
            path clonedPath = pulledPath; // Clones the last path
            clonedPath.addConnection(lastActorFilms.at(i), coactors.at(j)); // Adds the movie/actor connection to the cloned path
            if (coactors.at(j) == target) { // If we found our target return true
              foundPath = clonedPath;
              return true;
            }
            partialPaths.push_back(clonedPath); // Otherwise we add it to the queue and continue the loop
          }
        }
      }
    }
  }
  return false; // If we the while loop stops then we can conclude that there is no path
}
