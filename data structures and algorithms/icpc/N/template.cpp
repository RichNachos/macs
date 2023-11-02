#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

void solve() {

}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    
    ll x,y,r,b,n, l = 0, t = 0;
    cin>>x>>y>>r>>b;
    cin>>n;
    string moves;
    cin>>moves;

    b--;
    r--; 

    for (int i = 0; i < moves.size(); i++) {
        char c = moves[i];

        switch (c)
        {
        case 'W':
            y--;
            t = min(t, y);
            break;
        case 'A':
            x--;
            l = min(l, x);
            /* code */
            break;
        case 'S':
            y++;
            b = max(b, y);
            /* code */
            break;
        case 'D':
            x++;
            r = max(r, x);
            /* code */
            break;
        
        default:
            break;
        }
    }
    cout << (r - l + 1) * (b - t + 1) << endl;

    return 0;
}