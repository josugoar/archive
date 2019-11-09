import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
from collections import OrderedDict

# plt.style.use("seaborn")

pkm = pd.read_csv("pokemon.csv")
pkm_types = pd.concat([pkm["Type 1"], pkm["Type 2"]])

fig, ax = plt.subplots()

fig.tight_layout()
ax.grid(False)
ax.spines['top'].set_visible(False)
ax.spines['bottom'].set_visible(False)
ax.spines['right'].set_visible(False)
ax.spines['left'].set_visible(False)
plt.tick_params(axis='x', which='both', bottom=False, top=False, labelbottom=False)
plt.tick_params(axis='y', which='both', left=False, right=False, labelleft=True)

ax.set_title("Pokemon types")

types: dict = {}
for pkm_type in pkm_types:
    if type(pkm_type) is str:
        if pkm_type not in types:
            types[pkm_type] = 1
        else:
            types[pkm_type] += 1
types = OrderedDict(sorted(types.items(), key=lambda x: x[1]))

colors = ["#96D9D6", "#D685AD", "#735797", "#B7B7CE", "#F7D02C",
          "#6F35FC", "#705746", "#C22E28", "#B6A136", "#A33EA1",
          "#EE8130", "#E2BF65", "#A6B91A", "#F95587", "#7AC74C",
          "#A98FF3", "#A8A77A", "#6390F0"]

ax.barh(list(types.keys()), list(types.values()), color=colors, height=0.8, alpha=0.9)
for i, val in enumerate(types.values()):
    ax.text(val + 1, i, str(val), color='#000000', verticalalignment='center')

plt.show()
