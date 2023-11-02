#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

long long binpow(long long a, long long b) {
    long long res = 1;
    while (b > 0) {
        if (b & 1)
            res = res * a;
        a = a * a;
        b >>= 1;
    }
    return res;
}

int main() {
    // ios_base::sync_with_stdio(false);
    // cin.tie(NULL); 
    int n;
    cin >> n;

    ll cur = 1 << (2 * n);
    ll start = cur;
    ll bet = cur / 2;

    int w = 0, l = 0;
    vector<string> seq;
    
    string str;

    while(w < n - 1 && l < n - 1) {
        cout << bet << endl;
        string str;
        cin >> str;
        if (str == "Won") {
            w++;
            cur += bet;
        } else {
            l++;
            cur -= bet;
        }
        if (seq.size() == 0 || seq[seq.size() - 1] == str) {
            bet /= 2;
        }

        seq.push_back(str);
    }

    while(w != n && l != n) {
        if (l == n - 1) {
            bet = cur;
        } else {
            bet = (2 * start - cur);
        }
        cout << bet << endl;

        string str;
        cin >> str;
        if (str == "Won") {
            w++;
            cur += bet;
        } else {
            l++;
            cur -= bet;
        }
    }

    return 0;
}