#include "parseutils.h"
#include <vector>
#include <string>


using namespace std;

ParseUtils::ParseUtils()
{

}

static vector<string> split(char c, string s) {
    vector<string> result;
    size_t pos = 0;

    while(true) {
        size_t nextpos = s.find(c, pos + 1);
        if(nextpos == string::npos) {
            nextpos = s.length();
        }
        if(nextpos == pos + 1) {
            pos = nextpos;
            continue;
        }
        result.push_back(s.substr(pos + 1, nextpos - pos - 1));
        pos = nextpos;
    }
}
