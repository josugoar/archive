import numpy as np

class Node:
    # Initialize node
    def __init__(self, val, parent=None):
        # Node value
        self.val = val
        # Node left child
        self.left = None
        # Node right child
        self.right = None
        # Node parent
        self.parent = parent
    # Create new node with child value
    def insertNode(self, child):
        # Move left if child value is less than current node value
        if child < self.val:
            # Initialize node if left spot is empty
            if self.left is None:
                self.left = Node(child, self)
            else:
                self.left.insert(child)
        # Move right if child value is greater than current node value
        elif child > self.val:
            # Initialize node if right spot is empty
            if self.right is None:
                self.right = Node(child, self)
            else:
                self.right.insert(child)
    # Delete node
    def delNode(self, val):
        # Get given child value node
        if val < self.val:
            self.left.delNode(val)
            return
        if val > self.val:
            self.right.delNode(val)
            return
        # Replace two children node value with right children
        # greater value and delete replaced original node
        if self.left and self.right:
            successor = self.right.getMin()
            self.val = successor.val
            successor.delNode(successor.val)
        # Replace one child node with its child node
        elif self.left:
            self.replaceNode(self.left)
        elif self.right:
            self.replaceNode(self.right)
        # Delete no children node
        else:
            self.replaceNode(None)
    # Replace node
    def replaceNode(self, new_val=None):
        if self.parent.left == self:
            self.parent.left = new_val
        else:
            self.parent.right = new_val
    # Get minimum value
    def getMin(self):
        while self.left:
            self = self.left
        return self
    # Get maximum value
    def getMax(self):
        while self.right:
            self = self.right
        return self
    # Print tree
    def printTree(self):
        # Print values from left to right
        if self.left:
            self.left.printTree()
        print(self.val, end=" ")
        if self.right:
            self.right.printTree()

root = Node(val=12)
my_list = [1, 3, 5, 7, 9, 2, 4, 6, 8, 11, 22, 33, 44, 55, 99, 88, 77, 66, 55]
for n in my_list:
    root.insertNode(n)
root.delNode(1)
root.printTree()
