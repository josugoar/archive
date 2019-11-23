import math
import numpy as np
import pandas as pd
from cycler import cycler
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

# Get class names and number of tokens belonging to them
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

# Separate df by classes
def splitClasses(df, labels):
    classes: list = []
    temp_val = 0
    # Use value ranges for each class as loc
    for val in list(labels.values()):
        classes.append(df.loc[temp_val:(temp_val+val-1)])
        temp_val += val
    return classes

# Set class color relation
def assignColors(labels):
    # Define color assignment
    color = "rbgkm"
    colors: dict = {}
    for i in range(0, len(labels.keys())):
        colors[list(labels.keys())[i]] = color[i]
    return colors

# Plot classes
def plotClasses(classes, colors):
    # Range each class
    for i in range(0, len(classes)):
        # Get all x and y values, class label and class color
        ax.scatter(classes[i]["x"], classes[i]["y"], color=list(colors.values())[i], alpha=1, label=list(classes[i]["label"])[0], edgecolors="b")
    ax.legend()

# kNN
def predict(point, k):

    # Create dataframe with x, y, label and euclidean distance
    dist: list = []
    for i in range(0, len(df)):
        dist.append([df["x"][i], df["y"][i], df["label"][i], math.sqrt((point[0] - df["x"][i])** 2 + (point[1] - df["y"][i])** 2)])
    # Order dist by dist
    dist = pd.DataFrame(dist, columns=["x", "y", "label", "dist"]).sort_values(["dist"])
    # Reset indexing
    dist.index = pd.RangeIndex(len(dist.index))

    # Get k nearest neighbors
    nearest = dist.loc[0:(k - 1)].drop(["dist"], axis=1).sort_values(["label"])

    # Predict point class
    # Get nearest neighbors labels by number
    nearest_labels = dict(sorted(labelData(df=nearest).items(), key=lambda x: x[1]))
    # Get maximum repeating label
    if len(nearest_labels.keys()) > 1:
        if (list(nearest_labels.values())[-1] == list(nearest_labels.values())[-2]):
            point_class = None
        else:
            point_class = list(nearest_labels.keys())[-1]
    else:
        point_class = list(nearest_labels.keys())[-1]
    return point_class


df = readData(data_dir="_KNN/data.csv")
df_labels = labelData(df=df)
df_classes = splitClasses(df=df, labels=df_labels)
df_classes_colors = assignColors(labels=df_labels)

fig, ax = plt.subplots()
# Set limits
ax.set_xlim(-12, 12)
ax.set_ylim(0, 12)

plotClasses(classes=df_classes, colors=df_classes_colors)

# Get point class
point = [-3, 2]
point_class = predict(point=point, k=1)
print(f"point_class:\n{point_class}\n")

# Plot point if it belongs to a class
if point_class:
    ax.scatter(point[0], point[1], color=df_classes_colors[point_class], alpha=1)
else:
    ax.scatter(point[0], point[1], color="#ffffff", alpha=1)

for c_y, temp_y in enumerate(np.linspace(0, 12, 100)):
    for c_x, temp_x in enumerate(np.linspace(-12, 12, 100)):
        point = [temp_x, temp_y]
        point_class = predict(point=point, k=1)
        # * print(f"point_class:\n{point_class}\n")

        # Plot point if it belongs to a class
        if point_class:
            ax.scatter(point[0], point[1], color=df_classes_colors[point_class], alpha=0.5, marker="s", s=5)
        else:
            ax.scatter(point[0], point[1], color="#ffffff", alpha=0.5, marker="s", s=5)

# Display grid
plt.show()
