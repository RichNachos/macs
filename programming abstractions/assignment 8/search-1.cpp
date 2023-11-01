#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'knightlOnAChessboard' function below.
 *
 * The function is expected to return a 2D_INTEGER_ARRAY.
 * The function accepts INTEGER n as parameter.
 */
bool isInside(int n, int a, int b) {
    if (a < 0 || b < 0 || a >= n || b >= n) {
        return false;
    }
    return true;
}

int minMoves(int n, int a, int b) {
    queue<vector<int> > q;
    vector<int> startingPos = {0, 0, 0};
    q.push(startingPos);
    
    set<vector<int> > walkedPos;
    walkedPos.insert(startingPos);
   
    while (!q.empty()) {
        vector<int> currentPos = q.front();
        q.pop();
        
        if (currentPos[0] == n-1 && currentPos[1] == n-1) {
            return currentPos[2];
        }
        
        int x = currentPos[0];
        int y = currentPos[1];
        int movesCounter = currentPos[2];
        
        if (isInside(n, x+a, y+b)) {
            vector<int> possiblePos = {x+a, y+b, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x+a, y-b)) {
            vector<int> possiblePos = {x+a, y-b, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x-a, y+b)) {
            vector<int> possiblePos = {x-a, y+b, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x-a, y-b)) {
            vector<int> possiblePos = {x-a, y-b, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x+b, y+a)) {
            vector<int> possiblePos = {x+b, y+a, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x+b, y-a)) {
            vector<int> possiblePos = {x+b, y-a, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x-b, y+a)) {
            vector<int> possiblePos = {x-b, y+a, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
        if (isInside(n, x-b, y-a)) {
            vector<int> possiblePos = {x-b, y-a, movesCounter + 1};
            vector<int> pos = {possiblePos[0], possiblePos[1]};
            if (!walkedPos.count(pos)) {
                q.push(possiblePos);
                walkedPos.insert(pos);
            }
        }
    }
    return -1;
}

vector<vector<int>> knightlOnAChessboard(int n) {
    vector<vector<int> > result(n-1);
    for (int i = 1; i < n; i++) {
        for (int j = 1; j < n; j++) {
            result[i-1].push_back(minMoves(n, i, j));
        }
    }
    return result;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string n_temp;
    getline(cin, n_temp);

    int n = stoi(ltrim(rtrim(n_temp)));

    vector<vector<int>> result = knightlOnAChessboard(n);

    for (size_t i = 0; i < result.size(); i++) {
        for (size_t j = 0; j < result[i].size(); j++) {
            fout << result[i][j];

            if (j != result[i].size() - 1) {
                fout << " ";
            }
        }

        if (i != result.size() - 1) {
            fout << "\n";
        }
    }

    fout << "\n";

    fout.close();

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}
