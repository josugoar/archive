import matplotlib.animation as animation
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.collections import LineCollection


fig, (ax1, ax2) = plt.subplots(2, 1, sharex=True, sharey=True)
fig.text(x=0.03, y=0.5, s="Trigonometric waves", verticalalignment="center", rotation="vertical", weight="bold")

def sine(i, x):

    # Clear axis
    ax1.clear()
    # Set title
    ax1.set_title("Sine")
    # Add grid
    ax1.grid()
    # Remove x axis ticks
    ax1.xaxis.set_ticks_position("none")
    # Set y pivot
    ax1.axhline(0, color="#000000", linewidth=0.75)

    # Draw sine line
    y1 = np.sin(x)
    if ((len(x) <= color_range) and (len(x) > 1)):
        ax1.plot(x, y1, color="#ff0000")
    else:
        ax1.plot(x[len(x) - color_range ::], y1[len(y1) - color_range ::], color="#ff0000")
        ax1.plot(x[0: len(x) + 1 - color_range], y1[0: len(y1) + 1 - color_range], color="#000000")
    ax1.plot(x[-1], y1[-1], color="#ff0000", marker="o", markersize=5)
    ax1.axhline(y1[-1], color="#ff0000", linewidth=0.75)

    return ax1

def cos(i, x):

    # Clear axis
    ax2.clear()
    # Set title
    ax2.set_title("Cosine")
    # Add grid
    ax2.grid()
    # Set x axis shared ticks
    ax2.set_xticks(np.linspace(0, 4 * np.pi, 9))
    ax2.set_xticklabels(["0", "π/2", "π", "3π/2", "2π", "5π/2", "3π", "7π/2", "4π"])
    # Set shared limits
    ax2.set_xlim(0, 4*np.pi)
    ax2.set_ylim(-1, 1)
    # Set y pivot
    ax2.axhline(0, color="#000000", linewidth=0.75)

    # Draw cosine line
    y2 = np.cos(x)
    if ((len(x) <= color_range) and (len(x) > 1)):
        ax2.plot(x, y2, color="#0000ff")
    else:
        ax2.plot(x[len(x) - color_range ::], y2[len(y2) - color_range ::], color="#0000ff")
        ax2.plot(x[0: len(x) + 1 - color_range], y2[0: len(y2) + 1 - color_range], color="#000000")
    ax2.plot(x[-1], y2[-1], color="#0000ff", marker="o", markersize=5)
    ax2.axhline(y2[-1], color="#0000ff", linewidth=0.75)

    return ax2

def artist(i):

    max_x = i * 0.1
    x = np.arange(0, max_x + 0.1, 0.1)

    return [sine(i, x), cos(i, x)]

tick_range = 1000
color_range = 5
frames = len(np.arange(0, 4*np.pi, 0.1))

anim1 = animation.FuncAnimation(fig, artist, frames=frames, repeat=True)
plt.show()
