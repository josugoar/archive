# BINARY TREE SORT

import numpy as np

def binaryTree_sort(list):
    class Node:
        # Initialize node
        def __init__(self, val):
            self.val = val
            self.left = None
            self.right = None
        # Create empty node value node
        def insert(self, child):
            if child < self.val:
                if self.left is None:
                    self.left = Node(child)
                else:
                    self.left.insert(child)
            elif child > self.val:
                if self.right is None:
                    self.right = Node(child)
                else:
                    self.right.insert(child)
        # Print tree
        def printTree(self):
            if self.left:
                self.left.printTree()
            print(self.val, end=" ")
            if self.right:
                self.right.printTree()
    # Create binary tree and insert list values
    for i in range(0, len(list)):
        n = list[i]
        if i == 0:
            root = Node(n)
        else:
            root.insert(n)
    return root.printTree()


my_list = np.random.randint(0, 10, size=(10))
binaryTree_sort(list=my_list)
