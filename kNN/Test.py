import math
import pandas
import matplotlib.pyplot as plt
from collections import OrderedDict
class dataset:
    def __init__(self, dataDir, labelDir):
        self.readData = pandas.read_csv(dataDir)
        self.readLabel = pandas.read_csv(labelDir)
        self.class1 = [for i in range(0, len(self.readData)) if int(self.readLabel[i]) == 0  [self.readData["x"][i], self.readData["y"][i]]]
        self.class2 = [[self.readData["x"][i], self.readData["y"][i]] if int(self.readLabel[i]) == 1 for i in range(0, len(self.readData))]
class token:
    def __init__(self, k, point):
        self.coordinates = point
        self.near = self.__nearest(k)
    def __nearest(self, k):
        near: list = []
        for i in range(0, k):
            near[[keys[i][0], keys[i][1]]]
        return near
class KNN:
    def __init__(self, dataDir, labelDir, k, point=None):
        self.data = dataset(dataDir, labelDir)
        self.point = token(k, point)
        self.nearest = self.near()
    def showPlot(self):
        fig, ax = plt.subplots()
        ax.scatter(self.class1.keys(), self.class1.values(), color="#0000FF", alpha=0.5)
        ax.scatter(self.class2.keys(), self.class2.values(), color="#FF0000", alpha=0.5)
        if self.point:
            ax.scatter(self.nearest.keys(), self.nearest.values(), color="#000000", s=2.5)
            ax.scatter(self.point[0], self.point[1], color="#00FF00", s=2.5)
        plt.show()

grid1 = KNN(dataDir="_KNN/data.csv", labelDir="_KNN/label.csv", k=3, point=[1.5, 8.5])
grid1.showPlot()
