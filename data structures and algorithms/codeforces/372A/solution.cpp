#include <bits/stdc++.h>
using namespace std;

int n,a[500005],i,j,ans;

int main() {
    
    cin>>n; ans = n;
    for (i = 0; i < n; i++) {
        cin>>a[i];
    }
    
    i = 0; j = (n / 2) + (n % 2);
    sort(a, a + n);

    while (i != (n / 2) + (n % 2) && j != n) {
        if (a[i] * 2 <= a[j]) {
            i++; j++;
            ans--;
        } else {
            j++;
        }
    }
    cout<<ans<<endl;

    return 0;
}