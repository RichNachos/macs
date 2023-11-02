#include <bits/stdc++.h>
using namespace std;
#define ll long long

ll t[200005];
ll p = 998244353;
ll dp1[200005], dp2[200005];

ll power(ll, ll);
ll binomial(ll, ll);
void fill(ll);

int main() {
    fill_n(dp1, 200005, 1);
    fill_n(dp2, 200005, 1);

    ll n, result = 1;
    scanf("%lld", &n);
    for (ll i = 1; i <= n - 1; i++) {
        scanf("%lld", &t[i]);
    }
    fill(n);

    for (ll i = 1; i <= n - 2; i++) {
        result = result * binomial(t[i + 1] + 1, t[i]) % p;
    }

    printf("%lld\n", result);
    return 0;
}

ll power(ll a, ll n) {
    ll ans = 1;
    while (n != 0) {
        if (n % 2 == 1) {
            ans = ans * a % p;
        }
        a = a * a % p;
        n /= 2;
    }
    return ans;
}

ll binomial(ll n, ll k) {
    if (n < k) return 0;
    if (k < 0) return 0;
    return dp1[n] * dp2[k] % p * dp2[n - k] % p;
}

void fill(ll n) {
    for (ll i = 2; i <= n + 2; i++) {
        dp1[i] = dp1[i - 1] * i % p;
        dp2[i] = power(dp1[i], p - 2);
    }
}