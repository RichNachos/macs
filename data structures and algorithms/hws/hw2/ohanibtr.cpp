#include <bits/stdc++.h>
using namespace std;

struct node {
    int value;
    node* parent;
    node* left;
    node* right;
};

int minMoves(int[], int);
int create_tree(int depth);
//void read_tree(node* root);

int t,n,a[100002],ind,leafs,maxleafs;
vector<int> ans(100002);

int main() {
    scanf("%d", &t);
    for (int k = 1; k <= t; k++) {
        scanf("%d", &n);
        for (int i = 1; i <= n; i++) {
            scanf("%d", &a[i]);
        }
        printf("Case %d:\n", k);
        printf("Minimum Move: %d\n", minMoves(a, n));
        //sort(a + 1, a + n + 1);

        
        int size = n;
        int depth = 0;
        while (size != 0) {
            depth++;
            size = size >> 1;
        }
        //node* null_node = new node;
        //null_node->value = -1;
        ind = 1;
        leafs = 0;
        maxleafs = n - (1 << (depth - 1)) + 1;
        //node* root = create_tree(depth, null_node);
        int value = create_tree(depth);
        ans[value] = -1;
        //ans.clear(); // Clear answer of previous test case
        //read_tree(root);
        

        for (int i = 1; i <= n; i++) {
            if (i != n)
                printf("%d ", ans[i]);
            else
                printf("%d\n", ans[i]);
        }
    }
}

// Minimum moves needed to sort the array
int minMoves(int arr[], int size) {
    vector<int> sequences;
    sequences.push_back(-1);
    for (int j = 1; j <= size; j++) {
        int a = arr[j];
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
    return size - (sequences.size() - 1);
}

// int create_tree(int depth) {
//     if (depth == 0) return -1;
//     if (depth == 1 && maxleafs == leafs) return -1;
//     if (depth == 1) leafs++;
//     //node* new_node = new node;
//     //new_node->parent = parent;
//     /*new_node->left = */
//     int value = create_tree(depth - 1);
//     if (value > -1)
//         ans[value] = ind;
//     ind++; 
//     value = create_tree(depth - 1);
//     if (value > -1)
//         ans[value] = ind;
//     //cout<<new_node->value<<endl;
//     //new_node->right = create_tree(depth - 1, new_node);
//     //return new_node;
// }

int create_tree(int depth) {
    if (depth == 0) return -1;
    if (depth == 1 && maxleafs == leafs) return -1;
    if (depth == 1) leafs++;
    int idx;
    idx = create_tree(depth - 1);
    int self_val = ind++;
    if (idx > -1)
        ans[idx] = self_val;
    idx = create_tree(depth - 1);
    if (idx > -1)
        ans[idx] = self_val;
    return self_val;
}

/*
void read_tree(node* root) {
    if (root == NULL) return;
    read_tree(root->left);
    ans.push_back(root->parent->value);
    read_tree(root->right);
}
*/