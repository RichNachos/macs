#include <bits/stdc++.h>
#include <stack>
using namespace std;

stack<int> st;
int n,a;
string q;

int main() {
    cin>>n;
    for (int i = 0; i < n; i++) {
        cin>>q;
        if (q == "PUSH") {
            cin>>a;
            if (st.size() > 0 && st.top() < a) {
                st.push(st.top());
            } else {
                st.push(a);
            }
        }
        if (q == "POP") {
            if (st.size() > 0) {
                st.pop();
            } else {
                cout<<"EMPTY"<<endl;
            }
        }
        if (q == "MIN") {
            if (st.size() > 0) {
                cout<<st.top()<<endl;
            } else {
                cout<<"EMPTY"<<endl;
            }
            
        }

    }

    return 0;
}