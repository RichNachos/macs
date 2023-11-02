#include <bits/stdc++.h>
using namespace std;

int n,k,l,sum,a[500005];

int main() {
    cin>>n;
    for (int i = 0; i < n; i++) {
        cin>>a[i];
        sum += a[i];
    }
    if (sum % 3 != 0) {
        cout<<0<<endl;
    }

    sum = 0;
    for (int i = 0; i < n; i++) {
        sum += a[i];
        if (sum % 3 == 0) {
            k++;
            if (sum % 6 == 0) {
                
            }
        }
    }

}