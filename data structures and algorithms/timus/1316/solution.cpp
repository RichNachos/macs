#include <bits/stdc++.h>
using namespace std;

int tree[1000001], maxVal = 1000000, n;
double a,ans;
string q;

void update(int idx ,int val) {
    while (idx <= maxVal) {
        tree[idx] += val;
        idx += (idx & -idx);
    }
}

int read(int idx) {
    int sum = 0;
    while (idx > 0) {
        sum += tree[idx];
        idx -= (idx & -idx);
    }
    return sum;
}

int main() {
    while (true) {
        cin>>q;
        if (q == "QUIT") break;

        cin>>a; a = a * 100;
        if (q == "BID") {
            update(a, 1);
        }
        if (q == "DEL") {
            update(a, -1);
        }
        if (q == "SALE") {
            cin>>n;
            int sum = read(maxVal) - read(a-1);
            if (n > sum) {
                ans += sum;
            } else {
                ans += n;
            }
        }
    }
    cout << ans / 100 << endl;
}