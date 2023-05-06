import abc
import math
import random


def uct(node, exploration_weight=math.sqrt(2)):
    return (node.total_reward / node.visits + exploration_weight
            * math.sqrt(math.log(node.parent.visits) / node.visits))


def choice(state):
    return random.choice(state.actions)


class MCTS:

    def __init__(self, tree_policy=uct, default_policy=choice):
        self.tree_policy = tree_policy
        self.default_policy = default_policy

    def search(self, root):
        leaf = self._select(root)
        child = self._expand(leaf)
        reward = self._simulate(child.state)
        self._backpropagate(child, reward)
    
    def best_child(self, node):
        return max(node.children, key=self.tree_policy)

    def _select(self, node):
        while node.fully_expanded and not node.state.terminal:
            node = self.best_child(node)
        return node

    def _expand(self, node):
        if not node.state.terminal:
            action = node.choose()
            return node.add_child(action)
        return node

    def _simulate(self, state):
        while not state.terminal:
            action = self.default_policy(state)
            state = state.step(action)
        return state.reward

    def _backpropagate(self, node, reward):
        while node is not None:
            node.update(reward)
            node = node.parent
            reward *= -1


class Node:

    def __init__(self, state, parent=None):
        self.state = state
        self.parent = parent
        self.children = set()
        self.visits = 0
        self.total_reward = 0

    @property
    def fully_expanded(self):
        return len(self.children) == len(self.state.actions)

    def add_child(self, action):
        state = self.state.step(action)
        child = Node(state, parent=self)
        self.children.add(child)
        return child

    def choose(self):
        degree = len(self.children)
        return self.state.actions[degree]

    def update(self, reward):
        self.visits += 1
        self.total_reward += reward


class State(abc.ABC):

    @property
    @abc.abstractmethod
    def actions(self): ...

    @property
    @abc.abstractmethod
    def reward(self): ...

    @property
    @abc.abstractmethod
    def terminal(self): ...

    @abc.abstractmethod
    def step(self, action): ...
