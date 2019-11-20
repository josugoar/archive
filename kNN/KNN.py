import math
import pandas
import matplotlib.pyplot as plt
from collections import OrderedDict

k = 3
data = pandas.read_csv("_KNN/data.csv")
point = [1.190041585755129194e+00, 6.612370213365792893e+00]

dist: dict = {}
for i in range(0, len(data)):
    if data["x"][i] != point[0] and data["y"][i] != point[1]:
        dist[(data["x"][i], data["y"][i])] = math.sqrt((point[0] - data["x"][i]) ** 2 + (point[1] - data["y"][i]) ** 2)
dist = dict(OrderedDict(sorted(dist.items(), key=lambda x: x[1])))
keys = list(dist.keys())
vals = list(dist.values())

middle = (data["x"].max() + data["x"].min()) / 2
data_lower: dict = {}
data_upper: dict = {}
for i in range(0, len(data)):
	if data["x"][i] != point[0] and data["y"][i] != point[1]:
		if data["x"][i] < middle:
			data_lower[data["x"][i]] = data["y"][i]
		elif data["x"][i] > middle:
			data_upper[data["x"][i]] = data["y"][i]

nearest: dict = {}
for i in range(0, k):
	nearest[keys[i][0]] = keys[i][1]

lower = 0
upper = 0
for i in range(0, len(nearest)):
	if list(nearest.keys())[i] < middle:
		lower += 1
	elif list(nearest.keys())[i] < middle:
		upper += 1
if lower > upper:
	print("Point is BLUE")
elif lower < upper:
	print("Point is RED")

fig, ax = plt.subplots()
ax.axis("on")
ax.scatter(data_lower.keys(), data_lower.values(), color="#0000FF", alpha=0.5)
ax.scatter(data_upper.keys(), data_upper.values(), color="#FF0000", alpha=0.5)
ax.scatter(nearest.keys(), nearest.values(), color="#000000")
ax.scatter(point[0], point[1], color="#00FF00")
plt.show()
