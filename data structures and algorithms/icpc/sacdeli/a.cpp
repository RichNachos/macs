#include <bits/stdc++.h>
using namespace std;


int main() {

    long n;
    cin>>n;

    long l = 1, r = n;
    while(l < r) {
        long mid = (l + r) / 2 + (l + r) % 2;
        cout << "? " << mid << endl;

        string input;
        cin >> input;
        if (input == "<") {
            r = mid - 1;
        } else {
            l = mid;
        }
    }
    cout<<"! " << l <<endl;


    return 0;
}