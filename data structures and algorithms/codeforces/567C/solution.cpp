#include <bits/stdc++.h>
#include <map>
using namespace std;

long long n,k,a[200005],i;
map<long long,long long> lm,rm;

int main() {
    cin>>n>>k;
    for (i = 0; i < n; i++) {
        cin>>a[i];
        rm[a[i]]++;
    }
}