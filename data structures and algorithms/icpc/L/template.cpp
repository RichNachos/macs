#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

void solve() {

}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    
    bool maxx = true;
    string s;
    cin>>s;

    s[0] = '9';
    for (int i = 1; i < s.size(); i += 2) {
        char c = s[i];

        switch (c)
        {
        case '+':
            maxx = true;
            s[i+1] = '9';
            break;
        case '-':
            maxx = false;
            s[i+1] = '1';
            break;
        case '*':
            if (maxx)
                s[i+1] = '9';
            else
                s[i+1] = '1';
            break;
        case '/':
            s[i + 1] = '1';
            break;
        default:
            break;
        }
    }
    cout<<s<<endl;

    return 0;
}