#include <bits/stdc++.h>

using namespace std;

/*
 * Complete the 'superReducedString' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

string superReducedString(string s) {
    bool lastRemoved = true;
    string result = s;
    while (lastRemoved) {
        lastRemoved = false;
        if (result.length() == 0) {
            return "Empty String";
        }
        for (int i = 0; i < result.length() - 1; i++) {
            if (result[i] == result[i+1]) {
                lastRemoved = true;
                result = result.substr(0,i) + result.substr(i+2,result.length()-i-1);
                
                break;
            }
        }
    }
    return result;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s;
    getline(cin, s);

    string result = superReducedString(s);

    fout << result << "\n";

    fout.close();

    return 0;
}
