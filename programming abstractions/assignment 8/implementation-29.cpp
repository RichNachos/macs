#include <bits/stdc++.h>
#include <algorithm>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);
vector<string> split(const string &);

/*
 * Complete the 'surfaceArea' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts 2D_INTEGER_ARRAY A as parameter.
 */
 void printGrid(vector<vector<int> > A) {
     for (int i = 0; i < A.size(); i++) {
         for (int j = 0; j < A[0].size(); j++) {
             cout<<A[i][j]<<" ";
         }
         cout<<endl;
     }
 }
 
 int maxInt(int a, int b) {
     if (a > b) {
         return a;
     }
     return b;
 }

vector<vector<int> > getGridWithBorder(vector<vector<int> > A) {
    int h = A.size();
    int w = A[0].size();
    
    vector<vector<int> > gridWithBorder(h+2);
   
    vector<int> row(w+2, 0);
    for (int i = 0; i < h + 2; i++) {
        gridWithBorder[i] = row;
    }
    
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            gridWithBorder[i+1][j+1] = A[i][j];
        }
    }
    return gridWithBorder;
}

int surfaceArea(vector<vector<int>> A) {
    int h = A.size();
    int w = A[0].size();
    
    int surfaceArea = h * w * 2;
    vector<vector<int> > gridWithBorder = getGridWithBorder(A);
    printGrid(gridWithBorder);
    
    for (int i = 1; i <= h; i++) {
        for (int j = 1; j <= w; j++) {
            int currCell = gridWithBorder[i][j];
            
            surfaceArea += max(currCell - gridWithBorder[i-1][j], 0);
            surfaceArea += max(currCell - gridWithBorder[i][j+1], 0);
            surfaceArea += max(currCell - gridWithBorder[i][j-1], 0);
            surfaceArea += max(currCell - gridWithBorder[i+1][j], 0);
        }
    }
    return surfaceArea;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string first_multiple_input_temp;
    getline(cin, first_multiple_input_temp);

    vector<string> first_multiple_input = split(rtrim(first_multiple_input_temp));

    int H = stoi(first_multiple_input[0]);

    int W = stoi(first_multiple_input[1]);

    vector<vector<int>> A(H);

    for (int i = 0; i < H; i++) {
        A[i].resize(W);

        string A_row_temp_temp;
        getline(cin, A_row_temp_temp);

        vector<string> A_row_temp = split(rtrim(A_row_temp_temp));

        for (int j = 0; j < W; j++) {
            int A_row_item = stoi(A_row_temp[j]);

            A[i][j] = A_row_item;
        }
    }

    int result = surfaceArea(A);

    fout << result << "\n";

    fout.close();

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}

vector<string> split(const string &str) {
    vector<string> tokens;

    string::size_type start = 0;
    string::size_type end = 0;

    while ((end = str.find(" ", start)) != string::npos) {
        tokens.push_back(str.substr(start, end - start));

        start = end + 1;
    }

    tokens.push_back(str.substr(start));

    return tokens;
}
