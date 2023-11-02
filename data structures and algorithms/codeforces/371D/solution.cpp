#include <bits/stdc++.h>
using namespace std;

int n, capacity, q_count, q_type, v_num, water, capacities[200001], vessels[200001];
set<int> st;
set<int>::iterator it;

int main() {
    cin>>n;
    for (int i = 1; i <= n; i++) {
        cin>>capacity;
        capacities[i] = capacity;
        st.insert(i);
    }
    cin>>q_count;
    for (int i = 0; i < q_count; i++) {
        cin>>q_type;
        if (q_type == 1) {
            cin>>v_num>>water;
            it = st.lower_bound(v_num);
            while(water > 0) {
                if (it == st.end()) break;
                if (capacities[*it] - vessels[*it] <= water) {
                    water = water - (capacities[*it] - vessels[*it]);
                    vessels[*it] = capacities[*it];
                    it=st.erase(it++);
                    /*
                    st.erase(it);
                    it = st.lower_bound(v_num);
                    */
                } else {
                    vessels[*it] += water;
                    water = 0;
                }
            }
        }
        if (q_type == 2) {
            cin>>v_num;
            cout<<vessels[v_num]<<endl;
        }
    }
    return 0;
}

/*
TEST

11
7 16 12 9 6 14 23 19 17 13 10
8
1 5 28
2 2
1 1 25
1 2 23
2 2
1 3 30
2 5
2 8

*/