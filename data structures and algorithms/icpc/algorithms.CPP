//----------------------------------Segtree------------------------------------------------|

#include <bits/stdc++.h>
using namespace std;
  
// limit for array size
const int N = 100000; 
  
int n; // array size
  
// Max size of tree
int tree[2 * N];
  
// function to build the tree
void build( int arr[]) 
{ 
    // insert leaf nodes in tree
    for (int i=0; i<n; i++)    
        tree[n+i] = arr[i];
      
    // build the tree by calculating parents
    for (int i = n - 1; i > 0; --i)     
        tree[i] = tree[i<<1] + tree[i<<1 | 1];    
}
  
// function to update a tree node
void updateTreeNode(int p, int value) 
{ 
    // set value at position p
    tree[p+n] = value;
    p = p+n;
      
    // move upward and update parents
    for (int i=p; i > 1; i >>= 1)
        tree[i>>1] = tree[i] + tree[i^1];
}
  
// function to get sum on interval [l, r)
int query(int l, int r) 
{ 
    int res = 0;
      
    // loop to find the sum in the range
    for (l += n, r += n; l < r; l >>= 1, r >>= 1)
    {
        if (l&1) 
            res += tree[l++];
      
        if (r&1) 
            res += tree[--r];
    }
      
    return res;
}
  
// driver program to test the above function 
int main() 
{
    int a[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
  
    // n is global
    n = sizeof(a)/sizeof(a[0]);
      
    // build tree 
    build(a);
      
    // print the sum in range(1,2) index-based
    cout << query(1, 3)<<endl;
      
    // modify element at 2nd index
    updateTreeNode(2, 1);
      
    // print the sum in range(1,2) index-based
    cout << query(1, 3)<<endl;
  
    return 0;
}

|------------------------------------------------------------------------------------|

#define ull unsigned long long
#define ll long long

//-------------------------------------OST---------------------------------------------|


// OST
#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/tree_policy.hpp>
using namespace __gnu_pbds;
typedef tree<
int,
null_type,
less<int>,
rb_tree_tag,
tree_order_statistics_node_update>
ost;

//--------------------------------------DSU------------------------------------
void make_set(int v) {
    parent[v] = v;
}

int find_set(int v) {
    if (v == parent[v])
        return v;
    return find_set(parent[v]);
}

void union_sets(int a, int b) {
    a = find_set(a);
    b = find_set(b);
    if (a != b)
        parent[b] = a;
}

//-----------------------------------DSU-with-ranks-compression-----------------
void make_set(int v) {
    parent[v] = v;
    rank[v] = 0;
}

void union_sets(int a, int b) {
    a = find_set(a);
    b = find_set(b);
    if (a != b) {
        if (rank[a] < rank[b])
            swap(a, b);
        parent[b] = a;
        if (rank[a] == rank[b])
            rank[a]++;
    }
}

//----------------------------------BIT-------------------------------------------
struct FenwickTree {
    vector<int> bit;  // binary indexed tree
    int n;

    FenwickTree(int n) {
        this->n = n;
        bit.assign(n, 0);
    }

    FenwickTree(vector<int> a) : FenwickTree(a.size()) {
        for (size_t i = 0; i < a.size(); i++)
            add(i, a[i]);
    }

    int sum(int r) {
        int ret = 0;
        for (; r >= 0; r = (r & (r + 1)) - 1)
            ret += bit[r];
        return ret;
    }

    int sum(int l, int r) {
        return sum(r) - sum(l - 1);
    }

    void add(int idx, int delta) {
        for (; idx < n; idx = idx | (idx + 1))
            bit[idx] += delta;
    }
};

//-------------------------------binpow----------------------------------
long long binpow(long long a, long long b) {
    long long res = 1;
    while (b > 0) {
        if (b & 1)
            res = res * a;
        a = a * a;
        b >>= 1;
    }
    return res;
}