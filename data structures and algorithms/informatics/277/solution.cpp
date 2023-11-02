#include <bits/stdc++.h>
#include <deque>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false), cin.tie(0), cout.tie(0);
    int n,k,a;
    deque<pair<int,int>> q;
    cin>>n>>k;
    for (int i = 0; i < n; i++) {
        cin>>a;
        while (q.size() && q.back().first <= a)
            q.pop_back();
        q.push_back({a, i});
        while (q.front().second <= i - k)
            q.pop_front();
        if (i >= k - 1)
            cout<<q.front().first<<" ";
    }
    cout<<endl;


    return 0;
}