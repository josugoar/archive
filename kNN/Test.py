import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from collections import OrderedDict

# class kNN:
#     def __init__(self, k, dir_data, dir_label):
#         self.k = k
#         self.data = __split(__order(__read(dir_data, dir_label)))

#     @staticmethod
#     def __read(dir_data, dir_label):
#         read_data = pd.read_csv(dir_data)
#         read_label = pd.read_csv(dir_label)
#         return pd.concat([read_data, read_label], axis=1)

#     @staticmethod
#     def __order(data):
#         pass

#     @staticmethod
#     def __split(data):
#         types: dict = {}
#         for val in data["class"]:
#             if val in types:
#                 types[val] += 1
#             else:
#                 types[val] = 1
#         for i in range(0, data):
#             for j in range(0, len(types)):
#                 if data["class"] == j:
#                     pass

#     def showPlot(self):
#         pass

#     def locatePoint(self, point):
#         pass

# grid1 = kNN(k=3, dir_data="_KNN/data.csv", dir_label="_KNN/label.csv")
# grid1.showPlot()
# grid1.locatePoint(point=[1.5, 8.5])

read_data = pd.read_csv("_KNN/data.csv")
read_label = pd.read_csv("_KNN/label.csv")
dataset = pd.concat([read_data, read_label], axis=1).sort_values(["label"])

temp_labels: dict = {}
for label in dataset["label"]:
    if f"{label}" in temp_labels:
        temp_labels[f"{label}"] += 1
    else:
        temp_labels[f"{label}"] = 1
labels = dict(sorted(temp_labels.items()))

classes: list = []
temp_val = 0
for val in list(labels.values()):
    classes.append(dataset.loc[temp_val:temp_val+val])
    temp_val += val


# classes: list = []
# for i in range(0, len(labels)):
#     classes.append([])

# for i in range(0, len(dataset)):
#     for label in labels:
#         if dataset["label"][i] == label:
#             classes[label].append((dataset["x"], dataset["y"]))


print(dataset)
print(labels)
print(classes)
