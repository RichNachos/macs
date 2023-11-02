#include <bits/stdc++.h>
using namespace std;

stack<int> st;
int n,w,a,b,ans;

int main() {
    ios_base::sync_with_stdio(false),cin.tie(0),cout.tie(0);
    cin>>n>>w;
    st.push(0);

    for (int i = 0; i < n; i++) {
        cin>>a>>b;

        if (st.top() < b) {
            st.push(b);
            continue;
        }
        while (st.top() > b) {
            ans++;
            st.pop();
        }
        if (st.top() < b) {
            st.push(b);
        }
    }
    
    cout<<ans + st.size() - 1;
}