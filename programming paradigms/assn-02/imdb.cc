using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <cstring>

struct actorPair {
  const void *actorFile;
  const char *actorName;
};
struct filmPair {
  const void *movieFile;
  film movie;
};

int compareActors(const void *a, const void *b);
int compareMovies(const void *a, const void *b);

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const
{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

// you should be implementing these two methods right here... 
bool imdb::getCredits(const string& player, vector<film>& films) const {
  actorPair thisActor;
  thisActor.actorFile = actorFile;
  thisActor.actorName = player.c_str(); // Name is case sensitive (user should input proper format)

  const void *actorLoc = bsearch(&thisActor, (char*)actorFile + sizeof(int), *(int*)actorFile, sizeof(int), compareActors);
  if (actorLoc == NULL) // If no actor was found return false
    return false;

  int actorOffset = *(int*)actorLoc;
  const void *actorPtr = (char*)actorFile + actorOffset; // Points to start of actor info
  
  int length = player.size() + 1; // Length with '\0'
  if (length % 2 != 0) // Adds 1 to length if padding is needed
    length++;
  short actorMovieCount = *(short*)((char*)actorPtr + length); // Find how many movies this actor was in
  
  if (length % 4 != 0) // To avoid reading more paddings
    length += 2;
  for (int i = 0; i < actorMovieCount; i++) {
    int filmOffset = *(int*)((char*)actorPtr + length + i*sizeof(int)); // Gets actors film offset
    const void *filmPtr = (void*)((char*)movieFile + filmOffset); // Points to start of film in movieFile
    string filmName((char*)filmPtr); // Gets the name
    int filmYear = 1900 + *((char*)filmPtr + filmName.size() + 1); // Gets the year

    film f;
    f.title = filmName;
    f.year = filmYear;
    films.push_back(f);
  }
  return true;
}

bool imdb::getCast(const film& movie, vector<string>& players) const {
  filmPair thisMovie;
  thisMovie.movieFile = movieFile;
  thisMovie.movie = movie;

  const void *movieLoc = bsearch(&thisMovie, (char*)movieFile + sizeof(int), *(int*)movieFile, sizeof(int), compareMovies);
  if (movieLoc == NULL) // movieLoc points to offset of movie data
    return false;

  int movieOffset = *(int*)movieLoc;
  const void *moviePtr = (char*)movieFile + movieOffset; // Points to start of movie data
  
  int length = movie.title.size() + 1 + 1; // Length including null termination and single byte representing delta
  if (length % 2 != 0) // Check for additional padding
    length++;
  
  short movieActorCount = *(short*)((char*)moviePtr + length); // How many actors were in this movie
  if (length % 4 != 0) // Even more paddings...
    length += 2;

  for (int i = 0; i < movieActorCount; i++) {
    int actorOffset = *(int*)((char*)moviePtr + length + i*sizeof(int)); // Get actor offset
    string actorName = (char*)actorFile + actorOffset; // And get actor name

    players.push_back(actorName);
  }
  return true;
}

imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}

int compareActors(const void *a, const void *b) {
  actorPair *a1 = (actorPair*) a;
  const char *a1Name = a1->actorName;
  const void *actorFile = a1->actorFile;

  const char *a2Name = (char*)actorFile + *(int*)b;

  string s1(a1Name);
  string s2(a2Name);
  
  // cout << s1 << " " << s2 << endl; Check how bsearch works

  int compareInt = strcmp(a1Name, a2Name);
  return compareInt;
}

int compareMovies(const void *a, const void *b) {
  filmPair *a1 = (filmPair*) a;
  const void *movieFile = a1->movieFile;
  film movie1 = a1->movie;

  const void *movie2Loc = (char*)movieFile + *(int*)b;
  string movie2Name((char*)movie2Loc);
  int offset = movie2Name.size() + 1; 
  int movie2Year = 1900 + *((char*)movie2Loc + offset);
  
  film movie2;
  movie2.title = movie2Name;
  movie2.year = movie2Year;

  int compareInt = strcmp(movie1.title.c_str(), movie2.title.c_str());
  if (compareInt == 0)
    return (movie1.year > movie2.year) - (movie1.year < movie2.year);
  return compareInt;
}