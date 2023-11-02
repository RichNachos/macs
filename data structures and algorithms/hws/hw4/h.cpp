#include <bits/stdc++.h>
using namespace std;
 
int h[10005], e[1000005], ne[1000005],low[10005],dp[10005],scc[10005],rd[10005],cd[10005];
int n, m, a, b, idx, counter_scc, counter;
stack<int> st;
 
void dfs(int v) {
    counter++;
    low[v] = counter;
    dp[v] = counter;

    st.push(v);
    for (int i = h[v]; i; i = ne[i]) {
        int ii = e[i];
        if (!dp[ii]) {
            dfs(ii);
            low[v] = min(low[v], low[ii]);
        }
        else
            if (!scc[ii])
                low[v] = min(low[v], dp[ii]);
    }
    if (low[v] == dp[v]) {
        counter_scc++;
        int top = st.top();
        st.pop();
        scc[top] = counter_scc;
        while (v != top) {
            top = st.top();
            st.pop();
            scc[top] = counter_scc;
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0), cout.tie(0);
    cin >> n >> m;
    for (int i = 0; i < m; i++) {
        cin >> a >> b;
        idx++;
        e[idx] = b;
        ne[idx] = h[a];
        h[a] = idx;
    }
    for (int i = 1; i <= n; i++) {
        if (!dp[i]) {
            dfs(i);
        }
    }

    unordered_set<int> set;
    for (int i = 1; i <= n; i++) {
        for (int j = h[i]; j; j = ne[j]) {
            int a = scc[i], b = scc[e[j]];
            int hash  = (a * 10000000) + b;
            if (a != b && !set.count(hash)) {
                cd[a]++;
                rd[b]++;
                set.insert(hash);
            }
        }
    }
    int ans = 0, count1 = 0, count2 = 0;
    for (int i = 1; i <= counter_scc; ++i) {
        if (rd[i] == 0)
            count1++;
        if (cd[i] == 0)
            count2++;
    }
    
    ans = max(count1, count2);
    if (counter_scc == 1)
        cout << 0 << endl;
    else
        cout << ans << endl;

    return 0;
}