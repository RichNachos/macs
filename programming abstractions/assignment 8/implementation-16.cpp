#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'happyLadybugs' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING b as parameter.
 */
bool isAlreadyHappy(string b) {
    if (b[0] != '_' && b[0] != b[1]) {
        return false;
    }
    for (int i = 1; i < b.size() - 1; i++) {
        if (b[i] != b[i-1] && b[i] != b[i+1] && b[i] != '_') {
            return false;
        }
    }
    if (b[b.size() - 1] != b[b.size() - 2] && b[b.size() - 1] != '_') {
        return false;
    }
    return true;
}

bool isThereEmptyCell(string b) {
    for (int i = 0; i < b.size(); i++) {
        if (b[i] == '_') {
            return true;
        }
    }
    return false;
}

bool isThereNoLonelyBug(string b) {
    int arr[26] = {0};
    
    for (int i = 0; i < b.size(); i++) {
        if (b[i] != '_') {
            arr[b[i] - 'A']++;
        }
    }
    for (int i = 0; i < 26; i++) {
        if (arr[i] == 1) {
            return false;
        }
    }
    return true;
}

string happyLadybugs(string b) {
    bool emptyCellExists = isThereEmptyCell(b);
    bool bugsAreHappy = isAlreadyHappy(b);
    bool noLonelyBug = isThereNoLonelyBug(b);
    
    if ((emptyCellExists && noLonelyBug) || bugsAreHappy) {
        return "YES";
    }
    
    return "NO";
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string g_temp;
    getline(cin, g_temp);

    int g = stoi(ltrim(rtrim(g_temp)));

    for (int g_itr = 0; g_itr < g; g_itr++) {
        string n_temp;
        getline(cin, n_temp);

        int n = stoi(ltrim(rtrim(n_temp)));

        string b;
        getline(cin, b);

        string result = happyLadybugs(b);

        fout << result << "\n";
    }

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
