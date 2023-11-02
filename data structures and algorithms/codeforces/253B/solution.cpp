#include <bits/stdc++.h>
using namespace std;

int n,a[200005],i,j,ans;

int main() {
    ifstream cin("input.txt");
    ofstream cout("output.txt");

    cin>>n;
    for (i = 0; i < n; i++) {
        cin>>a[i];
    }
    sort(a, a + n);
    
    i = 0; j = 0; ans = n;
    while (j != n) {
        if (a[i]*2 < a[j]) {
            i++;
        } else {
            j++;
            ans = min(ans, n-j+i);
        }
    }
    cout<<ans<<endl;

    return 0;
}