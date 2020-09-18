import abc
import functools
import math
import random


def uct(node, child, exploration_weight=math.sqrt(2)):
    return (child.exploitation_component + exploration_weight
            * math.sqrt(math.log(node.visits) / child.visits))


class MCTS:

    def __init__(self, bandit_strategy=uct):
        self.bandit_strategy = bandit_strategy

    def search(self, root):
        leaf = self._tree_policy(root)
        reward = self._default_policy(leaf.state)
        self._backpropagate(leaf, reward)

    def best_child(self, node):
        partial_bandit_strategy = functools.partial(self.bandit_strategy, node)
        return max(node.children, key=partial_bandit_strategy)

    def _tree_policy(self, node):
        while not node.state.terminal:
            if not node.fully_expanded:
                return node.expand()
            node = self.best_child(node)
        return node

    def _default_policy(self, state):
        while not state.terminal:
            action = random.choice(state.actions)
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
