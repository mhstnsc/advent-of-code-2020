import re
from pprint import *

lines = open("/Users/mihai/projects/advent-of-code-2020/input02.txt").readlines()
matcher = re.compile(r"(?P<min>\d+)-(?P<max>\d+) (?P<c>[a-zA-Z]): (?P<pass>.*)")

parsed = [matcher.search(line).groupdict() for line in lines]

pprint(parsed)

result = 0

for line in parsed:
    minCount = int(line["min"])-1
    maxCount = int(line["max"])-1
    char = line["c"]
    passwd = line["pass"]

    if minCount < 0 or maxCount >= len(line):
        continue

    pos0 = passwd[minCount]
    pos1 = passwd[maxCount]
    if (pos0 == char and pos1 != char) or (pos0 != char and pos1 == char):
        result = result + 1

print(result)