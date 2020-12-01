# This is a sample Python script.

def sum3(theSum, input):
    for i in range(0, len(input)):
        for j in range(0, len(input)):
            for k in range(0, len(input)):
                if i != j and j != k and i != k:
                    current = input[i] + input[j] + input[k]
                    if current == 2020:
                        print(input[i]*input[j]*input[k])

lines = [int(x) for x in open("data.txt").readlines()]

sum3(2020, lines)
