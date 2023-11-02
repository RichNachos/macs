#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

void solve() {

}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    
    ll n,k,ans=1;
    cin>>n>>k;

    if (n % k != 0) {
        cout<<0<<endl;
        return 0;
    } else {
        for (int i = 1; i <= k; i++) {
            ans *= i;
        }
        cout << ans << endl;
    }

    return 0;
}