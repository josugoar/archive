import abc
import functools
import math
import random


def upper_confidence_trees(node, child, exploration_weight=math.sqrt(2)):
    return (child.total_reward / child.visits + exploration_weight
            * math.sqrt(math.log(node.visits) / child.visits))


def uniform_random_distribution(state):
    return random.choice(state.actions)


class MCTS:
    
    def __init__(self, bandit_strategy=upper_confidence_trees,
                 rollout_policy=uniform_random_distribution):
        self.bandit_strategy = bandit_strategy
        self.rollout_policy = rollout_policy

    def search(self, root):
        leaf = self._select(root)
        reward = self._rollout(leaf.state)
        self._backpropagate(leaf, reward)
    
    def best_child(self, node, **kwargs):
        partial_policy = functools.partial(self.bandit_strategy,
                                           node, **kwargs)
        return max(node.children, key=partial_policy)

    def _select(self, node):
        while not node.state.terminal:
            if not node.fully_expanded:
                return node.expand()
            node = self.best_child(node)
        return node

    def _rollout(self, state):
        while not state.terminal:
            action = self.rollout_policy(state)
            state = state.take_action(action)
        return state.reward

    def _backpropagate(self, node, reward):
        while node is not None:
            node.update(reward)
            node = node.parent


class MCTSNode:

    def __init__(self, state, parent=None):
        self.state = state
        self.parent = parent
        self.children = set()
        self.visits = self.total_reward = 0

    @property
    def fully_expanded(self):
        return len(self.children) == len(self.state.actions)

    def expand(self):
        action = self._next_action()
        child = self.take_action(action, children=False)
        self.children.add(child)
        return child

    def _next_action(self):
        degree = len(self.children)
        return self.state.actions[degree]

    def update(self, reward):
        self.visits += 1
        self.total_reward += reward[self.state.agent]

    def take_action(self, action, children=True):
        if children:
            for child in self.children:
                if child.state.action == action:
                    return child
        state = self.state.take_action(action)
        return MCTSNode(state, parent=self)


class MCTSState(abc.ABC):

    @property
    @abc.abstractmethod
    def agent(self): ...

    @property
    @abc.abstractmethod
    def reward(self): ...

    @property
    @abc.abstractmethod
    def terminal(self): ...

    @property
    @abc.abstractmethod
    def action(self): ...

    @property
    @abc.abstractmethod
    def actions(self): ...

    @abc.abstractmethod
    def take_action(self, action): ...
