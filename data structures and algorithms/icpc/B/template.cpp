#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long
int N;
vector<int> a;

void naivesol() {
    cout << "YES\n";
    for(int i = 0; i < N; ++i) {
        cout << "0 ";
    }
}

void panic() {
    cout << "NO\n";
}

void printYesAns(const vector<int>& ans) {
    cout << "YES\n";
    for(int i : ans) {
        cout << i << " ";
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    cin >> N;
    a.resize(N);
    int mn = INT_MAX;
    int mx = INT_MIN;
    for(int i = 0; i < N; ++i) {
        int curr; cin >> curr;
        a[i] = curr;
        mn = min(mn, curr);
        mx = max(mx, curr);
    }
    if(mn == mx) {
        naivesol();
        return 0;
    }

    vector<int> sorted(a);
    sort(sorted.begin(), sorted.end());

    int l = 0;
    int r = N - 1;
    vector<int> ans;
    ans.reserve(N);

    bool rev = false;
    for(int i = N - 1; i > 0; --i) {
        if(!rev) {
            if(sorted[r] != a[i]) {
                if(sorted[l] != a[i]) {
                    panic();
                    return 0;
                }
                rev = true;
                ans.push_back(1);
                l++;
            } else {
                rev = false;
                ans.push_back(0);
                r--;
            }
        } else {
            if(sorted[l] != a[i]) {
                if(sorted[r] != a[i]) {
                    panic();
                    return 0;
                }
                rev = false;
                ans.push_back(1);
                r--;
            } else {
                rev = true;
                ans.push_back(0);
                l++;
            }
        }
    }

    ans.push_back(0);
    
    reverse(ans.begin(), ans.end());

    printYesAns(ans);

    return 0;
}