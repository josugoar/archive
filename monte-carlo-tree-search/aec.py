from pettingzoo.classic import connect_four_v3

from uct import UCTSearch

ITERATIONS = 50

env = connect_four_v3.env(render_mode="rgb_array")
env.reset()

env2 = connect_four_v3.env()
env2.reset()

uct = UCTSearch(env2, "player_0", ITERATIONS, max_leaf_workers=None)

while True:
    print(env.render())

    _, _, terminated, truncated, _ = env.last()
    if terminated or truncated:
        break

    if env.agent_selection == "player_0":
        action = uct.step(None)
    elif env.agent_selection == "player_1":
        action = int(input())
        uct.step(action)

    env.step(action)
    env2.step(action)

env.close()
env2.close()
