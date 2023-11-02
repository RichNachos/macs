#include <bits/stdc++.h>
using namespace std;



int main() {

    int w,h,n,x,max_w = 1,max_h = 1, len = 1;
    char q;
    set<int> wst,hst;
    set<int>::iterator it,cpy;
    wst.insert(1); wst.insert(w);
    hst.insert(1); hst.insert(h);
    cin>>w>>h>>n;
    //max_w = w; max_h = h;
    for (int i = 0; i < n; i++) {
        cin>>q>>x;
        if (q == 'V') {
            wst.insert(x);
        }
        if (q == 'H') {
            hst.insert(x);
        }
    }
    cout<<max_w * max_h<<endl;

    return 0;
}