import abc
import math
import random
# https://www.scitepress.org/papers/2018/66535/66535.pdf

def uct(node, exploration_weight=math.sqrt(2)):
    return (node.exploitation_component + exploration_weight
            * math.sqrt(math.log(node.parent.visits) / node.visits))


def choice(state):
    return random.choice(state.actions)


class MCTS:

    def __init__(self, tree_policy=uct, default_policy=choice):
        self.tree_policy = tree_policy
        self.default_policy = default_policy

    def search(self, root):
        leaf = self._select(root)
        # TODO: Add inline expansion phase
        reward = self._simulate(leaf.state)
        self._backpropagate(leaf, reward)

    def best_child(self, node):
        return max(node.children, key=self.tree_policy)

    def _select(self, node):
        while not node.state.terminal:
            if not node.fully_expanded:
                return node.expand()
            node = self.tree_policy(node)
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


class Node:

    def __init__(self, state, parent=None):
        self.state = state
        self.parent = parent
        self.children = set()
        self.visits = self.total_reward = 0

    @property
    def exploitation_component(self):
        return (self.total_reward / self.visits
                if self.visits else float("-inf"))

    @property
    def fully_expanded(self):
        return len(self.children) == len(self.state.actions)

    def expand(self):
        action = self._find_action()
        child = self._take_action(action)
        self.children.add(child)
        return child

    def update(self, reward):
        self.visits += 1
        self.total_reward += reward[self.state.agent]

    def _find_action(self):
        degree = len(self.children)
        return self.state.actions[degree]

    def _take_action(self, action):
        state = self.state.take_action(action)
        return Node(state, parent=self)


class State(abc.ABC):

    @property
    @abc.abstractmethod
    def action(self): ...

    @property
    @abc.abstractmethod
    def actions(self): ...

    @property
    @abc.abstractmethod
    def agent(self): ...

    @property
    @abc.abstractmethod
    def observation(self): ...

    @property
    @abc.abstractmethod
    def reward(self): ...

    @property
    @abc.abstractmethod
    def terminal(self): ...

    @abc.abstractmethod
    def step(self, action): ...
