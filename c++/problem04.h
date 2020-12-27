#ifndef PROBLEM03_H
#define PROBLEM03_H

#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <map>
#include <regex>
#include <stdlib.h>
using namespace std;

struct Problem04
{
    ifstream f;

    int validpassport = 0;

    map<string, string> passport;
    set<string> validPassport = {"ecl", "pid", "eyr", "hcl", "byr", "iyr", "hgt"};

    function<bool(string)> byrValidator = [](string value) -> bool {
        std::regex r("\\d{4}");
        if(!regex_match(value, r)) return false;

        int num = atoi(value.c_str());
        return (num >= 1920 && num <= 2002);
    };
    function<bool(string)> iyrValidator = [](string value) -> bool {
        std::regex r("\\d{4}");
        if(!regex_match(value, r)) return false;

        int num = atoi(value.c_str());
        return (num >= 2010 && num <= 2020);
    };
    function<bool(string)> eyrValidator = [](string value) -> bool {
        std::regex r("\\d{4}");
        if(!regex_match(value, r)) return false;

        int num = atoi(value.c_str());
        return (num >= 2020 && num <= 2030);
    };

    function<bool(string)> hgtValidator = [](string value) -> bool {
        if(regex_match(value, std::regex("\\d+ ?cm"))) {
            int num = atoi(value.c_str());
            return (num >= 150 && num <= 193);
        }
        if(regex_match(value, std::regex("\\d+ ?in"))) {
            int num = atoi(value.c_str());
            return num >= 59 && num <= 76;
        }
        return false;
    };
    function<bool(string)> hclValidator = [](string value) -> bool {
        return regex_match(value, std::regex("\\#([0-9a-f]{6})"));
    };

    function<bool(string)> eclValidator = [](string value) -> bool {
        return regex_match(value, std::regex("(amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth)"));
    };

    function<bool(string)> pidValidator = [](string value) -> bool {
        return regex_match(value, std::regex("\\d{9}"));
    };

    map<string, function<bool(string)>> validators = {
        {"ecl", eclValidator},
        {"pid", pidValidator},
        {"eyr", eyrValidator},
        {"hcl", hclValidator},
        {"byr", byrValidator},
        {"iyr", iyrValidator},
        {"hgt", hgtValidator},
    };

    void checkPassport() {
        set<string> diff;
        set<string> passportKeys;
        for(auto it : passport)
            passportKeys.insert(it.first);

        set_difference(
            validPassport.begin(),
            validPassport.end(),
            passportKeys.begin(),
            passportKeys.end(),
            inserter(diff, end(diff))
        );
        if(diff.size() > 0)
            return;

        // check each field
        for(auto it: passport) {
            auto validator = validators.find(it.first);
            if(validator != validators.end()) {
                if(!((validator->second)(it.second)))
                    return;
            }
        }
        validpassport++;
    }



    Problem04() {
        auto f = ifstream("/Users/mihai/projects/advent-of-code-2020/04/input.txt");

        cout << "byr:" <<  byrValidator("1910") <<  byrValidator("1920") << byrValidator("19203") << endl;
        cout << "iyr:" <<  iyrValidator("1910") <<  iyrValidator("2020") << iyrValidator("19203") << endl;
        cout << "eyr:" <<  eyrValidator("1910") <<  eyrValidator("2020") << eyrValidator("19203") << endl;
        cout << "hgt:" <<  hgtValidator("150cm") <<  hgtValidator("59in") << hgtValidator("19203") << endl;
        cout << "hcl:" <<  hclValidator("#12345a") <<  hclValidator("12345af") << hclValidator("#23123fd") << endl;
        cout << "ecl:" <<  eclValidator("blu") <<  eclValidator("12313") << eclValidator("19203") << endl;
        cout << "pid:" <<  pidValidator("123456789") <<  pidValidator("12333") << pidValidator("2331") << endl;

        string line;
        while(getline(f, line)) {
            if(line.size() == 0) {
                checkPassport();
                passport.clear();
                continue;
            }

            size_t pos = 0;
            while(true) {
                pos = line.find(':', pos + 1);
                if(pos == string::npos)
                    break;

                size_t valueEnd = line.find(' ', pos + 1);
                if(valueEnd == string::npos)
                    valueEnd = line.size();

                passport[line.substr(pos - 3, 3)] = line.substr(pos + 1, valueEnd - (pos + 1));
            }
        }
        checkPassport();
        cout << "P1: " << validpassport << endl;
    }
};

#endif // PROBLEM03_H
