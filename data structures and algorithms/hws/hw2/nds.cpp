#include <bits/stdc++.h>
using namespace std;

int t,n,a,l;

int main() {
    scanf("%d", &t);
    for (int i = 0; i < t; i++) {
        scanf("%d", &n);
        if (n == 0) {
            scanf("%d", &l);
            printf("%d\n", -1);
            continue;
        }

        vector<int> sequences;
        sequences.push_back(-1);
        for (int j = 0; j < n; j++) {
            scanf("%d", &a);
            int low, high;
            low = 0;
            high = sequences.size();
            while (low < high) {
                int mid = low + high;
                mid /= 2;
                if (sequences[mid] < a) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            if (low == sequences.size()) {
                sequences.push_back(a);
            } else {
                sequences[low] = a;
            }
        }
        
        scanf("%d", &l);
        if (sequences.size() <= l) {
            printf("%d\n", -1);
        } else {
            printf("%d\n", sequences[l]);
        }
        
    }
}