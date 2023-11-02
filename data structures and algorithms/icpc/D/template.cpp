#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
#define ll long long

typedef pair< pair<int, int>, pair<int, int> > ndt;
vector<vector<pair<int,int>>> graph;
int n, m;

void initgraph(){
    for(int i = 0; i < m; ++i) {
        int v, t, c;
        cin >> v >> t >> c;
        v--; t--;
        graph[v].push_back(make_pair(t, c));
        graph[t].push_back(make_pair(v, c));
    }
}

pair<pair<int, int>, pair<int, int>> createnode(int cost, int dist, int loc, int color) {
    return make_pair(make_pair(cost, dist), make_pair(loc, color));
}

void printans(const pair<pair<int, int>, pair<int, int>>& node){
    if(node.first.first == 0) {
        cout << "YES " << -node.first.second << endl;
    } else {
        cout << "NO " << -node.first.first << endl;
    }
}

void printnode(const pair<pair<int, int>, pair<int, int>>& node){
    printf("%d, %d, %d, %d\n", node.first.first, node.first.second, node.second.first, node.second.second);
}

vector<vector<bool>> been;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); 
    cin >> n >> m;
    graph.resize(n);
    been.resize(n, vector<bool>(2, false));
    initgraph();
    
    priority_queue<ndt> pq;
    pq.push(createnode(0, 0, 0, 0));
    pq.push(createnode(0, 0, 0, 1));
    
    while(true){
        pair< pair<int, int>, pair<int, int> > curr = pq.top();
        pq.pop();
        
        if(been[curr.second.first][curr.second.second]) {
            continue;
        }
        been[curr.second.first][curr.second.second] = true;
        if(curr.second.first == n - 1) {
            printans(curr);
            return 0;
        }
        for (pair<int, int> to : graph[curr.second.first]) {
            int newCost = curr.first.first;
            if(to.second == curr.second.second) {
                newCost--;
            }
            int newDist = curr.first.second - 1;
            pq.push(createnode(newCost, newDist, to.first, to.second));
        }
    }

    return 0;
}