class Node:
    # Initialize node
    def __init__(self, val=None, left=None, right=None, parent=None):
        # Node value
        self.val = val
        # Node left child
        self.left = left
        # Node right child
        self.right = right
        # Node parent
        self.parent = parent
    # Create new node with child value and no grandchildren
    def insertNode(self, child):
        # Move left if child value is less than current node value
        if child < self.val:
            # Initialize node if left spot is empty
            if not self.left:
                self.left = Node(val=child, parent=self)
            else:
                self.left.insertNode(child=child)
        # Move right if child value is greater than current node value
        elif child > self.val:
            # Initialize node if right spot is empty
            if not self.right:
                self.right = Node(val=child, parent=self)
            else:
                self.right.insertNode(child=child)
    # Create nodes with values of given list
    def insertNodes(self, children):
        for child in children:
            if self.val:
                self.insertNode(child=child)
            else:
                self.val = child
    # Delete node
    def deleteNode(self, val):
        # Search for given child value node
        if val < self.val:
            if self.left:
                self.left.deleteNode(val=val)
            return
        if val > self.val:
            if self.right:
                self.right.deleteNode(val=val)
            return
        # Root
        if not self.parent:
            if not self.left:
                successor = self.right.getMin()
            elif not self.right:
                successor = self.left.getMax()
            else:
                successor = self.right.getMin()
            self.val = successor.val
            successor.deleteNode(val=successor.val)
        # Children
        if self.parent:
            # Replace two children node value with right children
            # greater value node and delete replaced original node
            if self.left and self.right:
                successor = self.right.getMin()
                self.val = successor.val
                successor.deleteNode(val=successor.val)
            # Replace one child node with its child node
            elif self.left:
                self.__replaceNode(new_val=self.left)
            elif self.right:
                self.__replaceNode(new_val=self.right)
            # Delete no children node
            else:
                self.__replaceNode(None)
    # Replace node (deleteNode_aux)
    def __replaceNode(self, new_val=None):
        if self.parent.left == self:
            self.parent.left = new_val
        else:
            self.parent.right = new_val
        if new_val:
            new_val.parent = self.parent
    # Get minimum value node
    def getMin(self):
        while self.left:
            self = self.left
        return self
    # Get maximum value node
    def getMax(self):
        while self.right:
            self = self.right
        return self
    # Print sorted tree
    def printTree_sorted(self, depth=0):
        # Print values from left to right
        if self.left:
            self.left.printTree_sorted(depth=depth+1)
        print(self.val, end=" ")
        if self.right:
            self.right.printTree_sorted(depth=depth+1)
        if depth == 0:
            print("")
    # Print tree leafs
    def printTree_leaves(self):
        lines, _, _, _ = self.__printTree_leaves_magnitudes()
        for line in lines:
            print(line)
    # Get tree node magnitudes (printTree_leaves_aux)
    def __printTree_leaves_magnitudes(self):
        # Return no children node magnitudes
        if not self.right and not self.left:
            line = str(self.val)
            width = len(line)
            height = 1
            x = width // 2
            return [line], width, height, x
        # Return left child node magnitudes
        elif not self.right:
            lines, width, height, x = self.left.__printTree_leaves_magnitudes()
            n = str(self.val)
            off = len(n)
            first_line = (x + 1) * " " + (width - x - 1) * "_" + n
            second_line = x * " " + "/" + (width - x - 1 + off) * " "
            shifted_lines = [line + off * " " for line in lines]
            return [first_line, second_line] + shifted_lines, width + off, height + 2, width + off // 2
        # Return right child node magnitudes
        elif not self.left:
            lines, width, height, x = self.right.__printTree_leaves_magnitudes()
            n = str(self.val)
            off = len(n)
            first_line = n + x * "_" + (width - x) * " "
            second_line = (off + x) * " " + "\\" + (width - x - 1) * " "
            shifted_lines = [off * " " + line for line in lines]
            return [first_line, second_line] + shifted_lines, width + off, height + 2, off // 2
        # Calculate lines depending on parent nodes
        l_lines, l_width, l_height, l_x = self.left.__printTree_leaves_magnitudes()
        r_lines, r_width, r_height, r_x = self.right.__printTree_leaves_magnitudes()
        n = str(self.val)
        off = len(n)
        first_line = (l_x + 1) * " " + (l_width - l_x - 1) * "_" + n + r_x * "_" + (r_width - r_x) * " "
        second_line = l_x * " " + "/" + (l_width - l_x - 1 + off + r_x) * " " + "\\" + (r_width - r_x - 1) * " "
        if l_height < r_height:
            l_lines += [l_width * " "] * (r_height - l_height)
        elif l_height > r_height:
            r_lines += [r_width * " "] * (l_height - r_height)
        zipped_lines = zip(l_lines, r_lines)
        lines = [first_line, second_line] + [a + off * " " + b for a, b in zipped_lines]
        return lines, l_width + r_width + off, max(l_height, r_height) + 2, l_width + off // 2
class Tree:
    # Initialize tree root
    def __init__(self, root=None):
        self.root = Node(val=root)
    # Class methods
    def insertNode(self, child):
        self.root.insertNode(child=child)
    def insertNodes(self, children):
        self.root.insertNodes(children=children)
    def min(self):
        return self.root.getMin().val
    def max(self):
        return self.root.getMax().val
    def delete(self, val):
        self.root.deleteNode(val=val)
    def sort(self):
        self.root.printTree_sorted()
    def print(self):
        self.root.printTree_leaves()

import random

tree = Tree()
tree.insertNodes(children=[random.randint(0, 75) for i in range(0, 75)])

# tree.delete(val=1)
# tree.sort()
tree.print()
