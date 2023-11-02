#include <bits/stdc++.h>
using namespace std;

long long n,k,l,r,a[200005];
map<long long, long long> mp;
map<long long, long long>::iterator it;

int main() {
    
    cin>>n;
    for (int i = 0; i < n; i++) {
        cin>>l>>r;
        mp[l]++;
        mp[r]--;
    }
    
    for (it = mp.begin(); it != mp.end(); it++) {
        k += it->first
    }

    return 0;
}