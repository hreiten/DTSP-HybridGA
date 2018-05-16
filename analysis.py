import numpy as np

# read file
def readData(filename):
    data = []
    for line in reversed(open(filename).readlines()):
        try:
            isdigit = float(line.strip("\n"))
        except:
            isdigit = False
        if (isdigit):
            data.append(isdigit)
        if ("Run Stats for experiment at:" in line):
            break
    return(data[::-1])

data = readData("results.out");

mean = 0.0;
highest = max(x for x in data)
lowest = min(x for x in data)
mean = np.mean(data);

print("lowest:\t\t" + str(lowest))
print("mean:\t\t" + str(mean))
print("highest:\t" + str(highest))
