#include <bits/stdc++.h>
using namespace std;

int t,n;
string s;
bool flag;
int main() {
    cin>>t;
    while (t--) {
        cin>>n>>s;
        flag = true;
        for (int i = 1; i < n; i+=3) {
            if (i == n - 1 || s[i] != s[i+1]) {
                flag = false;
                break;
            }
        }
        if (flag)
            cout<<"YES"<<endl;
        else
            cout<<"NO"<<endl;
        
    }
}