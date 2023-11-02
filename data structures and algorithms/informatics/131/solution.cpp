#include <bits/stdc++.h>
#include <set>
using namespace std;

int main() {
    int n,a;
    set<int> st;
    cin>>n;
    for (int i = 1; i <= n + 1; i++) {
        st.insert(i);
    }
    for (int i = 1; i <= n; i++) {
        cin>>a;
        st.erase(a);
    }
    cout<<*st.begin()<<endl;


    return 0;
}