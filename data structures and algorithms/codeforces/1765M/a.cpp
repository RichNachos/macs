#include <bits/stdc++.h>
using namespace std;

int t,n,a,b=INT32_MAX;

int main() {
    cin>>t;
    while(t--) {
        cin>>n;
        if (n % 2 == 0) {
            cout<<n / 2<<" "<<n / 2<<endl;
            continue;
        }
        b = INT32_MAX;
        for (int i = 1; i <= sqrt(n); i++) {
            if (n % i == 0) {
                cout<<i<<"--"<<n/i<<"-";
                if (n - i < b) {
                    b = n - i;
                    a = i;
                }
                if (n - (n/i) < b) {
                    b = max(n - (n/i), n - i);
                    a = n/i;
                }
                cout<<b<<endl;
            }
        }
        cout<<a<<" "<<b<<endl;
    }
}