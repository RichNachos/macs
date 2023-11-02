#include <bits/stdc++.h>
using namespace std;

vector<string> permutations;

void solve();
void add_permutations(vector<string> &p, string key);

int main() {
    ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
    add_permutations(permutations, "");
    int t;
    cin>>t;
    while(t--) {
        solve();
    }
    return 0;
}

void add_permutations(vector<string> &p, string key) {
    if (key.size() == 5) {
        p.push_back(key);
        return;
    } else {
        for (int i = 0; i < 4; i++) {
            add_permutations(p, key + (char)('A' + i));
        }
    }
}

void solve() {
    int n;
    cin>>n;
    vector<string> s;
    vector<int> a;
    //string s[20001];
    //int a[20001];
    int result = 0;
    unordered_map<string, int> frq;
    
    for (int i = 0; i < n; i++) {
        string x; int y;
        cin>>x>>y;
        s.push_back(x);
        a.push_back(y / 10);
    }
    
    for (int i = 0; i < permutations.size(); i++) {
        string p = permutations[i];
        string hashable = "";
        bool f = false;
        for (int j = 0; j < n; j++) {
            int mark = 0;
            string answer = s[j];
            for (int k = 5; k < 10; k++) {
                if (p[k-5] == answer[k]) {
                    mark++;
                }
            }

            if (mark <= a[j]) {
                hashable += (char)('0' + mark);
            } else {
                f = true;
            }
        }
        if (!f) {
            frq[hashable]++;
        }
    }

    for (int i = 0; i < permutations.size(); i++) {
        string p = permutations[i];
        string hashable = "";
        bool f = false;
        for (int j = 0; j < n; j++) {
            string answer = s[j];
            int mark = 0;
            for (int k = 0; k < 5; k++) {
                if (p[k] == answer[k]) {
                    mark++;
                }
            }

            if (mark <= a[j]) {
                hashable += (char)('0' + (a[j] - mark));
            } else {
                f = true;
            }
        }
        if (!f) {
            result += frq[hashable];
        }
    }

    cout<<result<<endl;
}
