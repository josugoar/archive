import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
N = 100
rand = np.random.randint(0, 500, size=N)
fig, ax = plt.subplots(figsize=(5, 5))
def init():
    ax.set_facecolor("#000000")
    return ax,
def artists(i):
    ls = []
    for j in range(0, N - 1):
        if rand[j] > rand[j + 1]:
            temp = rand[j]
            rand[j] = rand[j + 1]
            rand[j + 1] = temp
            ls.append(j)
    ax.clear()
    ax.set_title("Bubble Sort")
    ax.bar(np.arange(N), rand, color="#FFFFFF", width=0.5)
    ax.bar(ls, rand[ls], color="#AA0000", width=0.5)
    return ax,
anim = animation.FuncAnimation(fig, artists, init_func=init, repeat=False, blit=False)
plt.show()
