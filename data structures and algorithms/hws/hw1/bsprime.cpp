#include <bits/stdc++.h>
using namespace std;
#define MAX_NUM 101865020


int answers[150000005],ind,size,j;
long long n,t,ans_index;
bool bits[30];

void addAnswer(int p) {
    ind = 0, size = 0;

    while (p != 0) {
        bits[size++] = p & 1;
        p = p >> 1;
    }
    for (j = size - 1; j >= 0; j--) {
        ans_index++;
        if (ans_index > 150000000) {
            return;
        }
        answers[ans_index] = answers[ans_index - 1] + bits[j];
    }
}

// nawilobriv gadmokopirebulia geeksforgeeks.org -dan
// aqve mivamateb rom gcc (c++) compilerze gadis, meore g++ compilerze time limit exceeds wers

void simpleSieve(int limit, vector<int> &prime)
{
    vector<bool> mark(limit + 1, true);
    
    for (int p=2; p*p<limit; p++)
    {
        if (mark[p] == true)
        {
            for (int i=p*p; i<limit; i+=p)
                mark[i] = false;
        }
    }
 
    for (int p=2; p<limit; p++)
    {
        if (mark[p] == true)
        {
            prime.push_back(p);
            addAnswer(p);
        }
    }
}
 
void segmentedSieve(int n)
{
    int limit = floor(sqrt(n))+1;
    vector<int> prime;
    simpleSieve(limit, prime);
 
    int low = limit;
    int high = 2*limit;
 
    while (low < n)
    {
        if (high >= n)
           high = n;
         
        bool mark[limit+1];
        memset(mark, true, sizeof(mark));
 
        for (int i = 0; i < prime.size(); i++)
        {
            int loLim = floor(low/prime[i]) * prime[i];
            if (loLim < low)
                loLim += prime[i];

            for (int j=loLim; j<high; j+=prime[i])
                mark[j-low] = false;
        }
 
        for (int i = low; i<high; i++)
            if (mark[i - low] == true)
                addAnswer(i);
 
        low = low + limit;
        high = high + limit;
    }
}

int main() {
    segmentedSieve(MAX_NUM);

    scanf("%lld",&t);
    for (int i = 0; i < t; i++) {
        scanf("%lld",&n);
        printf("%d\n", answers[n]);
    }
}
