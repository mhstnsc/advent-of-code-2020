#ifndef PARSEUTILS_H
#define PARSEUTILS_H

#include <vector>
#include <string>

using namespace std;


class ParseUtils
{
public:
    ParseUtils();

    static vector<string> split(char c, string s);
};

#endif // PARSEUTILS_H
