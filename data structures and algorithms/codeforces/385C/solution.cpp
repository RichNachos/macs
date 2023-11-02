#include <bits/stdc++.h>
using namespace std;

int isPrime(int n) {
    if (n < 2) return 0;
    if (n == 2) return 1;
    for (int i = 2; i < n; i++) {
        if (n%i==0)
            return 0;
    }
    return 1;
}

int main() {
    long long n, elems[1000006],q_count, l, r, sum = 0;
    map<long long, long long> mp;
    map<long long, long long>::iterator it;
    cin>>n;
    for (int i = 0; i < n; i++) {
        cin>>elems[i];
        long long tmp = elems[i];
        for (long long j = 2; j <= tmp; j++) {
            if (isPrime(j) && tmp % j == 1) {
                if (mp.find(j) == mp.end()) {
                    mp.insert(j, (long long)1);
                } else {
                    it = mp.find(j);
                    it->second++;
                }
            }
        }
    }
    cin>>q_count;
    for (long long i = 0; i < q_count; i++) {
        cin>>l>>r; sum = 0;
        it = mp.lower_bound(l);
        while (it != mp.upper_bound(r) || it != mp.end()) {
            sum += it->second;
            it++;
        }
        cout<<sum<<endl;
    }

    return 0;
}