import numpy as np
import sys

args = [arg for arg in sys.argv]


def readData(filename):
    data = []
    for line in reversed(open(filename).readlines()):
        try:
            input = float(line.strip("\n"))
            data.append(input)
        except:
            raise Exception("Error converting input to float")

        if ("Run Stats for experiment at:" in line):
            break
    return(data[::-1])


data = readData(args[1])

highest = max(x for x in data)
lowest = min(x for x in data)
mean = np.mean(data)

print("lowest:\t\t" + str(lowest))
print("mean:\t\t" + str(mean))
print("highest:\t" + str(highest))
