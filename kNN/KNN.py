import math
import pandas
import matplotlib.pyplot as plt
from collections import OrderedDict

data = pandas.read_csv("_kNearest/data.csv")

k = 3
point = [1.190041585755129194e+00, 6.612370213365792893e+00]
dist: dict = {}
for i in range(0, len(data)):
    if data["x"][i] != point[0] and data["y"][i] != point[1]:
        dist[(data["x"][i], data["y"][i])] = math.sqrt((point[0] - data["x"][i]) ** 2 + (point[1] - data["y"][i]) ** 2)
dist = dict(OrderedDict(sorted(dist.items(), key=lambda x: x[1])))
keys = list(dist.keys())
vals = list(dist.values())
print(vals)

fig, ax = plt.subplots()
ax.axis("off")
ax.scatter(data["x"], data["y"], alpha=0.5)
ax.scatter(point[0], point[1], color="#FF0000")
for i in range(0, k):
    ax.scatter(keys[i][0], keys[i][1], color="#311f1f")
plt.show()
