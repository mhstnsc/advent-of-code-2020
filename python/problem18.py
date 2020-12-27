import re
from pprint import *

lines = open("/Users/mihai/projects/advent-of-code-2020/input18.txt").readlines()


def compute(expr):
    replaced = expr.replace("+", "v").replace("*", "+").replace("v", "*")
    return int(eval(replaced))

print(compute("1 + (2 * 3) + (4 * (5 + 6))"))

acc = 0
for line in lines:

    acc += compute(line)

print(acc)