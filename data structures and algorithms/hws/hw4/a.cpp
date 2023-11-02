#include<bits/stdc++.h>
using namespace std;
#define ll long long

ll n, k, l, ans, maxx, i;
ll arr1[100005], arr2[100005], vals[100005], prefix[100005], suffix[100005];
pair<int,int> pairs[100005];
set<pair<int,int>> s, t;

int main() {
    cin >> n;
    for(i = 0; i < n; i++) 
        cin >> arr1[i];
    for(i = 0; i < n; i++) 
        cin >> arr2[i];
    cin >> k >> l;
    
    for(i = 0; i < k; i++)
        pairs[i] = {arr2[i], i};
    
    sort(pairs, pairs + k);
    for(i = k - l; i < k; i++)
        ans += pairs[i].first;

    s = {pairs + k - l, pairs + k};
    t = {pairs, pairs + k - l};
    
    vals[0] = ans;
    for(i = 1; i <= k; i++) {
        pair<int,int> p1 = {arr2[k - i], k - i};
        pair<int,int> p2 = {arr2[n - i], n - i};

        s.insert(p2);
        ans += p2.first;
        auto itr = s.find(p1);

        if (itr != s.end()) {
            ans -= itr->first;
            s.erase(itr);
            pair<int,int> a = *s.begin();
            pair<int,int> b = *t.rbegin();

            if(a < b){
                s.erase(s.begin());
                t.erase(prev(t.end()));
                s.insert(b);
                t.insert(a);
                ans += (b.first - a.first);
            }
        } else {
            t.erase(p1);
            t.insert(*s.begin());
            ans -= s.begin()->first;
            s.erase(s.begin());
        }

        vals[i] = ans;
    }
 
    prefix[0] = 0;
    suffix[0] = 0;
    for(i = 1; i <= k; i++){
        prefix[i] = arr1[i-1] + prefix[i-1];
        suffix[i] = arr1[n-i] + suffix[i-1];
    }
    
    for(i = 0; i <= k; i++)
        maxx = max(vals[i] + (prefix[k - i] + suffix[i]), maxx);
    
    cout << maxx << endl;
    return 0;
}