__credits__ = "https://gist.github.com/qpwo/c538c6f73727e254fdc7fab81024f6e1"

import threading
import time

from mcts import MCTS, Node, State

SYMBOLS = {True: "X", False: "O", None: "-"}

WINNING_COMBOS = ((0, 1, 2), (3, 4, 5), (6, 7, 8),
                  (0, 3, 6), (1, 4, 7), (2, 5, 8),
                  (0, 4, 8), (2, 4, 6))


class TicTacToeState(State):

    def __init__(self, board, turn, winner):
        self.board = board
        self.turn = turn
        self.winner = winner

    @property
    def reward(self):
        if self.winner is None:
            reward = [0.5] * 2
        else:
            reward = [1] * 2
            reward[self.winner] = 0
        return reward[0]

    @property
    def terminal(self):
        return self.winner is not None or not self.actions

    @property
    def actions(self):
        return [idx for idx, value in enumerate(self.board) if value is None]

    def step(self, action):
        board = list(self.board)
        board[action] = self.turn
        return TicTacToeState(board, not self.turn, _winner(board),)

    def _string(self, value):
        if value is True:
            return "X"
        if value is False:
            return "O"
        return "-"

    def __str__(self):
        string = ""
        for row in range(3):
            for col in range(3):
                value = self.board[3 * row + col]
                string += SYMBOLS[value]
                if col < 2:
                    string += " "
            string += "\n"
        return string


# QUICK AND DIRTY TICTACTOE
def main(rounds):
    tree = MCTS()
    root = _root()
    while True:
        print(root.state)
        move = input("Enter move: ")
        row, col = map(int, move.split())
        idx = 3 * row + col - 4
        if root.state.board[idx] is not None:
            raise ValueError("Invalid move")
        root = root.add_child(idx)
        if root.state.terminal:
            print("You win")
            print(root.state)
            break
        start = time.time()
        for _ in range(rounds):
            tree.search(root)
        print(f"Elapsed time: {time.time() - start} seconds")
        root = tree.best_child(root)
        if root.state.terminal:
            print("You lose")
            print(root.state)
            break


def _root():
    state = TicTacToeState([None] * 9, True, None)
    return Node(state)


def _winner(board):
    for i1, i2, i3 in WINNING_COMBOS:
        v1, v2, v3 = board[i1], board[i2], board[i3]
        if False is v1 is v2 is v3:
            return False
        if True is v1 is v2 is v3:
            return True
    return None


if __name__ == "__main__":
    main(10000)
