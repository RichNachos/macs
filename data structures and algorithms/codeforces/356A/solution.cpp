#include <bits/stdc++.h>
using namespace std;

int main() {
    int n,m,sol[300001] = {};
    set<int> s;
    set<int>::iterator it, it_copy;
    cin>>n>>m;
    for (int i = 1; i <= n; i++) {
        s.insert(i);
    }
    for (int i = 1; i <= m; i++) {
        int l,r,x;
        cin>>l>>r>>x;
        
        it = s.lower_bound(l);
        while (it != s.upper_bound(r) && it != s.end()) {
            it_copy = it; it_copy++;
            if (*it != x) {
                sol[*it] = x;
                s.erase(it);
            }
            it = it_copy;
        }
    }

    for (int i = 1; i <= n; i++) {
        cout<<sol[i]<<" ";
    }
    cout<<endl;
}