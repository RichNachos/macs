#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    ll n, d;
    cin >> n >> d;
    vector<ll> inlist;
    for(int i = 0; i < n; i++) {
        ll a;
        cin >> a;
        inlist.push_back(a);
    }
    sort(inlist.begin(), inlist.end());
    vector<ll> list;
    map<ll, ll> freq;
    for (int i = 0; i < inlist.size(); i++) {
        freq[inlist[i]]++;
        if (freq[inlist[i]] == 1) {
            list.push_back(inlist[i]);
        }
    }
    ll mn, mx, mnind = 0, mxind = list.size() - 1;
    mn = list[0];
    mx = list[mxind];
    ll totCost = 0;

    while(mx - mn > d) {
        ll costmn = freq[mn];
        ll costmx = freq[mx];
        
        if (costmn > costmx) {
            int diff =  min(mx - list[mxind - 1], mx - mn - d);
            totCost += diff * costmx;
            mx = list[mxind - 1];
            mxind--;
            freq[mx] += costmx;
        } else {
            int diff = min(list[mnind + 1] - mn, mx - mn - d);
            totCost += diff * costmn;
            mn = list[mnind + 1];
            mnind++;
            freq[mn] += costmn;
        }
    }

    cout << totCost << endl;
    return 0;
}