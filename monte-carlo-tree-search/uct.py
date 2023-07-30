import concurrent.futures
import math
import pickle
from typing import Any, Generic, TypeVar, cast

import numpy as np
import pettingzoo

ActionType = TypeVar("ActionType")
AgentID = str


class _UCTNode(Generic[ActionType]):

    def __init__(self, env: pettingzoo.AECEnv, parent: "_UCTNode[ActionType] | None", action: ActionType | None, mask: np.ndarray[Any, np.int8], done: bool, agent: AgentID) -> None:
        self.env = env
        self.agent = agent
        self.action = action
        self.mask = mask
        self.done = done
        self.parent = parent
        self.children: dict[ActionType | None, _UCTNode[ActionType]] = {}
        self.visits = 0
        self.reward = 0.0


class UCTSearch(Generic[ActionType]):

    def __init__(self, env: pettingzoo.AECEnv, agent: AgentID, max_iter: int, c: float = 1 / math.sqrt(2), max_leaf_workers: int | None = 0) -> None:
        self.env = env
        self.agent = agent
        self.max_iter = max_iter
        self.c = c
        self.max_leaf_workers = max_leaf_workers
        self._root: _UCTNode[ActionType] | None = None

    def step(self, action: ActionType | None) -> ActionType:
        if self._root is None:
            observation, _, termination, truncation, info = self.env.last()
            done = termination or truncation
            if "action_mask" in info:
                mask = info["action_mask"].copy()
            elif isinstance(observation, dict) and "action_mask" in observation:
                mask = observation["action_mask"].copy()
            else:
                mask = np.ones(self.env.action_space(self.env.agent_selection).n, dtype=np.int8)
            self._root = _UCTNode[ActionType](env=pickle.dumps(self.env), agent=self.env.agent_selection, action=None, mask=mask, done=done, parent=None)
        if action is None:
            for _ in range(self.max_iter):
                node = self._tree_policy(self._root)
                if self.max_leaf_workers is not None and self.max_leaf_workers <= 0:
                    reward = self._default_policy(pickle.loads(node.env))
                else:
                    with concurrent.futures.ProcessPoolExecutor(self.max_leaf_workers) as executor:
                        rewards = executor.map(self._default_policy, (pickle.loads(node.env) for _ in range(executor._max_workers))) # type: ignore[attr-defined]
                    reward = sum(rewards)
                self._backup(node, reward)
            action = self._best_child(self._root, 0).action
            action = cast(ActionType, action)
        self._root = self._root.children.get(action)
        return action

    def _tree_policy(self, node: _UCTNode[ActionType]) -> _UCTNode[ActionType]:
        while not node.done and node.agent == self.agent:
            if any(node.mask) or node.done:
                return self._expand(node)
            else:
                node = self._best_child(node, self.c)
        return node

    def _expand(self, node: _UCTNode[ActionType]) -> _UCTNode[ActionType]:
        env = pickle.loads(node.env)
        _, _, termination, truncation, _ = env.last(observe=False)
        done = termination or truncation
        if done:
            action = None
        else:
            action = env.action_space(env.agent_selection).sample(node.mask)
            node.mask[action] = False
        env.step(action)
        observation, _, termination, truncation, info = env.last()
        done = termination or truncation
        if "action_mask" in info:
            mask = info["action_mask"].copy()
        elif isinstance(observation, dict) and "action_mask" in observation:
            mask = observation["action_mask"].copy()
        else:
            mask = np.ones(env.action_space(env.agent_selection).n, dtype=np.int8)
        child = _UCTNode[ActionType](env=pickle.dumps(env), agent=env.agent_selection, action=action, mask=mask, done=done, parent=node)
        node.children[action] = child
        return child

    def _best_child(self, node: _UCTNode[ActionType], c: float) -> _UCTNode[ActionType]:
        return max(node.children.values(), key=lambda child: child.reward / child.visits + c * math.sqrt(2 * math.log(child.parent.visits) / child.visits)) # type: ignore[union-attr]

    def _default_policy(self, env: pettingzoo.AECEnv) -> float:
        while True:
            observation, reward, termination, truncation, info = env.last()
            done = termination or truncation
            if done:
                if env.agent_selection == self.agent:
                    break
                else:
                    action = None
            else:
                if "action_mask" in info:
                    mask = info["action_mask"]
                elif isinstance(observation, dict) and "action_mask" in observation:
                    mask = observation["action_mask"]
                else:
                    mask = None
                action = env.action_space(env.agent_selection).sample(mask)
            env.step(action)
        return reward

    def _backup(self, node: _UCTNode[ActionType] | None, reward: float) -> None:
        while node is not None:
            node.visits += 1
            node.reward += reward
            node = node.parent
