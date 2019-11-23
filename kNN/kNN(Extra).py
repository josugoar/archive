import math
import pandas
import matplotlib.pyplot as plt
from collections import OrderedDict

# Read data
data = pandas.read_csv("_kNN/data.csv")

# Order data from closest to furthest of distance between points
def order(point):
    dist: dict = {}
    for i in range(0, len(data)):
        dist[(data["x"][i], data["y"][i])] = math.sqrt((point[0] - data["x"][i]) ** 2 + (point[1] - data["y"][i]) ** 2)
    dist = OrderedDict(sorted(dist.items(), key=lambda x: x[1]))
    return list(dist.keys())

# Define separation criteria (red and blue points)
def separation(point, criteria):
    data_lower: dict = {}
    data_upper: dict = {}
    for i in range(0, len(data)):
        if data["x"][i] < criteria:
            data_lower[data["x"][i]] = data["y"][i]
        elif data["x"][i] > criteria:
            data_upper[data["x"][i]] = data["y"][i]
    return data_lower, data_upper

# Get k nearest neighbors
def near(keys, k):
    nearest: dict = {}
    for i in range(0, k):
        nearest[keys[i][0]] = keys[i][1]
    return nearest

# Define selected point to analize color depending on neighbors
def area(nearest, middle):
    lower = 0
    upper = 0
    for i in range(0, len(nearest)):
        if list(nearest.keys())[i] < middle:
            lower += 1
        elif list(nearest.keys())[i] > middle:
            upper += 1
    if lower > upper:
        print("Point is BLUE")
        return "blue"
    elif lower < upper:
        print("Point is RED")
        return "red"

# Plot data
def grid(data_lower, data_upper, point, nearest):
    fig, ax = plt.subplots()
    ax.axis("on")
    ax.scatter(data_lower.keys(), data_lower.values(), color="#0000FF", alpha=0.5)
    ax.scatter(data_upper.keys(), data_upper.values(), color="#FF0000", alpha=0.5)
    ax.scatter(nearest.keys(), nearest.values(), color="#000000", s=2.5)
    ax.scatter(point[0], point[1], color="#00FF00", s=2.5)
    plt.show()

# Main
def main(k, point, plot=False):
    if point[0] in data["x"] and point[1] in data["y"]:
        print("Point already exists...")
        return 0
    middleX = (data["x"].max() + data["x"].min()) / 2
    middleY = (data["y"].max() + data["y"].min()) / 2
    coordinates = order(point)
    data_lower, data_upper = separation(point, middleX)
    nearest = near(coordinates, k)
    area(nearest, middleX)
    if plot:
        grid(data_lower, data_upper, point, nearest)

# k: nearest neighbor number
# point: point to analize
# plot: show grid
if __name__ == "__main__":
    main(k=3, point=[1.5, 8.5], plot=True)
