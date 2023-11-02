#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

#define A 11ll

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    
    string q;
    cin>>q;
    if (q == "Alice") {
        long long n;
        cin>>n;

        if(n == 0) {
            cout << 0 << endl;
            return 0;
        }

        string inbinary = "";
        while (n != 0) {
            inbinary += '0' + (n % A);
            n /= A;
        }

        for (ll i = 0; i < inbinary.size() / 2; i++) {
            swap(inbinary[i], inbinary[inbinary.size() - 1 - i]);
        }

        for (ll i = 0; i < inbinary.size(); i++) {
            inbinary[i]++;
        }

        string ans = "";

        for (ll i = 0; i < inbinary.size(); i++) {
            for(ll j = 0; j < inbinary[i] - '0'; j++) {
                ans += '0' + i;
            }
        }
        cout << ans << endl;
    } else {
        string str;
        cin>>str;
        ll arr[10] = {0};

        for (ll i = 0; i < str.size(); i++) {
            arr[str[i] - '0']++;
        }
        for(ll i = 0; i < 10; i++) {
            arr[i]--;
        }

        ll ans = 0;
        for(ll i = 0; i < 10; i++) {
            if(arr[i] < 0) {
                break;
            }

            ans = A * ans + arr[i];
        }

        cout<<ans<<endl;
    }

    return 0;
}