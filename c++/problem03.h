#ifndef PROBLEM03_H
#define PROBLEM03_H

#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

struct Problem03
{
    vector<string> field;

    Problem03() {
        auto f = ifstream("/Users/mihai/projects/advent-of-code-2020/03/p1/input.txt");

        string line;
        while(getline(f, line))
            field.push_back(line);

        cout << "first" << numTrees(3,1) << endl;

        long result = numTrees(1,1) * numTrees(3,1) * numTrees(5,1) * numTrees(7,1) * numTrees(1,2);
        cout << "total multiplication:" << result << endl;

    }

    long numTrees(int stepX, int stepY) {
        int numLines = field.size();
        int numCols = field[0].size();
        int numTrees = 0;
        int cX = 0;
        int cY = 0;
        while(true) {
            cY += stepY;
            cX = (cX + stepX) % numCols;
            if(cY >= numLines)
                break;
            if(field[cY][cX] == '#')
                numTrees++;
        }
        return numTrees;
    }


};

#endif // PROBLEM03_H
