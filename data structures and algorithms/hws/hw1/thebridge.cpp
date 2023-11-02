#include <bits/stdc++.h>
#include <math.h>
using namespace std;

int n,a[100001],ans;

int main() {
    scanf("%d",&n);

    for (int i = 0; i < n; i++) {
        scanf("%d", &a[i]);
    }

    while (true) {
        if (n == 1) { // Base case for only 1 tourist
            ans += a[0];
            break;
        }
        if (n == 2) { // Base case for only 2 tourists
            ans += max(a[0], a[1]);
            break;
        }
        if (n == 3) { // Base case for only 3 tourists
            ans += max(a[0], a[2]) + a[0] + max(a[0], a[1]);
            break;
        }
        if (n >= 4) { // Check case 1 and case 2
            int c1 = 0,c2 = 0;
            c1 = max(a[0], a[1]) + a[0] + max(a[n-1], a[n-2]) + a[1];
            c2 = max(a[0], a[n-1]) + a[0] + max(a[0], a[n-2]) + a[0];
            ans += min(c1,c2);
            n-=2;
            continue;
        }
    }

    printf("%d\n",ans);
}