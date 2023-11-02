#include <bits/stdc++.h>
using namespace std;

int main() {
    long long n,q,elems[200005] = {},data[200005] = {}, q_type,k,x, prev_x = -1;
    long long sum = 0;
    set<int> st;
    set<int>::iterator it;
    cin>>n>>q;
    for (int i = 1; i <= n; i++) {
        cin>>elems[i];
        data[i] = elems[i];
        sum += elems[i];
    }
    for (int i = 0; i < q; i++) {
        cin>>q_type;
        if (q_type == 1) {
            cin>>k>>x;
            if (st.find(k) != st.end()) {
                sum += x - elems[k];
            } else {
                if (prev_x == -1) sum += x - elems[k];
                else              sum += x - prev_x;
                st.insert(k);
            }
            elems[k] = x;
        } else if (q_type == 2) {
            cin>>x;
            prev_x = x;
            sum = x * n;
            st.clear();
        }
        cout<<sum<<endl;
    }
    

    return 0;
}