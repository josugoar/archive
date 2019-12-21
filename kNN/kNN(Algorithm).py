import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from collections import OrderedDict

# Read df
    # df: DataFrame(x, y, label)
def readData(data_dir):
    # Order df by label
    df = pd.read_csv(data_dir).sort_values(["label"])
    # Reset indexing
    df.index = pd.RangeIndex(len(df.index))
    return df

# Get class names and number of points belonging to them
def labelData(df):
    temp_labels: dict = {}
    for label in df["label"]:
        if f"{label}" in temp_labels:
            temp_labels[f"{label}"] += 1
        else:
            temp_labels[f"{label}"] = 1
    # Order labels by label
    labels = dict(sorted(temp_labels.items(), key=lambda x: x[0]))
    return labels

# Set class color relation
def assignColors(labels):
    # Define color assignment
    color = "rbgkm"
    colors = {list(labels.keys())[i]:color[i] for i in range(0, len(labels.keys()))}
    return colors

# colors = {list(labels.keys())[i]:color[i] for i in range(0, len(labels.keys()))}

# Separate df by classes
def splitClasses(df, labels):
    classes: list = []
    temp_val = 0
    # Use value ranges for each class as loc
    for val in list(labels.values()):
        classes.append(df.loc[temp_val:(temp_val+val-1)])
        temp_val += val
    return classes

# Plot classes
def plotClasses(classes, colors):
    # Range each class
    for i in range(0, len(classes)):
        # Get all x and y values, class label and class color
        ax.scatter(classes[i]["x"], classes[i]["y"], color=list(colors.values())[i], alpha=1, label=list(classes[i]["label"])[0], edgecolors="#000000")
    ax.legend()

# Get nearest neighbor labels
def euclideanDistance(df, point, k):
    # Create dataframe with x, y, label and euclidean distance
    dist = [[df["x"][i], df["y"][i], df["label"][i], math.sqrt((point[0] - df["x"][i])** 2 + (point[1] - df["y"][i])** 2)] for i in range(0, len(df))]
    # Order dist by dist
    dist = pd.DataFrame(dist, columns=["x", "y", "label", "dist"]).sort_values(["dist"])
    # Reset indexing
    dist.index = pd.RangeIndex(len(dist.index))
    return dist

# dist = [[df["x"][i], df["y"][i], df["label"][i], math.sqrt((point[0] - df["x"][i])** 2 + (point[1] - df["y"][i])** 2)] for i in range(0, len(df))]

# Get k nearest neighbors labels
def nearPoint(dist, k):
    nearest = dist.loc[0:(k - 1)].drop(["dist"], axis=1).sort_values(["label"])
    # Order nearest labels by number
    nearest_labels = dict(sorted(labelData(df=nearest).items(), key=lambda x: x[1]))
    return nearest_labels

# Get maximum repeating label
def compareNear(nearest_labels):
    # k >= 1
    if len(nearest_labels.keys()) > 1:
        # Same distance
        if (list(nearest_labels.values())[-1] == list(nearest_labels.values())[-2]):
            point_class = None
        # Different distance
        else:
            point_class = list(nearest_labels.keys())[-1]
    # k = 1
    else:
        point_class = list(nearest_labels.keys())[-1]
    return point_class

# Predict point class
def predict(point, k):
    dist = euclideanDistance(df=df, point=point, k=k)
    nearest_labels = nearPoint(dist=dist, k=k)
    point_class = compareNear(nearest_labels)
    return point_class

# Voronoi diagram visualization
def voronoiDiagram(k, pixels):
    for temp_y in np.linspace(0, 12, pixels):
        for temp_x in np.linspace(-12, 12, pixels):
            point = [temp_x, temp_y]
            point_class = predict(point=point, k=k)
            # Plot point if it belongs to a class
            if point_class:
                ax.scatter(point[0], point[1], color=df_classes_colors[point_class], alpha=0.5, marker="s", s=7.5)

df = readData(data_dir="_kNN/data copy.csv")
df_labels = labelData(df=df)
df_classes = splitClasses(df=df, labels=df_labels)
df_classes_colors = assignColors(labels=df_labels)

fig, ax = plt.subplots()
# Set limits
ax.set_xlim(-12, 12)
ax.set_ylim(0, 12)

plotClasses(classes=df_classes, colors=df_classes_colors)
voronoiDiagram(k=1, pixels=50)

# Display grid
plt.show()
