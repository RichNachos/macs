#include <bits/stdc++.h>

using namespace std;

/*
 * Complete the 'makingAnagrams' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. STRING s1
 *  2. STRING s2
 */

int makingAnagrams(string s1, string s2) {
    int arr1[26] = {0};
    int arr2[26] = {0};
    
    
    ofstream fout(getenv("OUTPUT_PATH"));
    for (int i = 0; i < s1.length(); i++) {
        char c = tolower(s1[i]);
        arr1[c - 'a']++;
        
    }
    for (int i = 0; i < s2.length(); i++) {
        char c = tolower(s2[i]);
        arr2[c - 'a']++;
    }
    
    int result = 0;
    for (int i = 0; i < 26; i++) {
        result += abs(arr1[i] - arr2[i]);
    }
    return result;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s1;
    getline(cin, s1);

    string s2;
    getline(cin, s2);

    int result = makingAnagrams(s1, s2);

    fout << result << "\n";

    fout.close();

    return 0;
}
