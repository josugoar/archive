package _DeustoCrush;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

/**
 * DeustoCrush
 */
public class DeustoCrush {

    private static final int GRID_SIZE = 6;
    private static final int MIN_IN_ROW = 3;
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));

    private Candy[][] grid;

    {
        this.randomize();
    }

    public static final void main(final String[] args) {
        try {
            new DeustoCrush().run();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public final String toString() {
        String out = "";

        for (int i = 0; i < GRID_SIZE; i++) {
            out += String.format("   %d", i);
        }
        out += "\n";

        for (int i = 0; i < GRID_SIZE; i++) {
            out += i;

            for (int j = 0; j < GRID_SIZE; j++) {
                out += String.format(" %s", this.getGrid()[i][j]);
            }
            out += "\n";

        }

        return out;
    }

    private final void run() throws Exception {
        while (true) {
            System.out.println(this);

            final String[] strs = BR.readLine().split(" ");
            final int[][] points = new int[2][2];
            for (int i = 0; i < 2; i++) {
                points[i] = Stream.of(strs[i].split("-")).mapToInt(Integer::parseInt).toArray();
            }
            System.out.println("");

            this.swap(this.getGrid()[points[0][0]][points[0][1]], this.getGrid()[points[1][0]][points[1][1]]);
            for (int i = 0; i < 2; i++) {
                this.delete(this.check(this.getGrid()[points[i][0]][points[i][1]]));
            }

            this.goDown();

            System.out.println("");
        }
    }

    private final void randomize() {
        this.grid = new Candy[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new Candy(new Point(i, j));
            }
        }
    }

    private final boolean swap(final Candy c1, final Candy c2) {
        for (int i = c1.getSeed().x - 1; i <= c1.getSeed().x + 1; i++) {
            for (int j = c1.getSeed().y + Math.abs(c1.getSeed().x - i) - 1; j <= c1.getSeed().y + 1; j += 2) {

                if (i >= 0 && i < GRID_SIZE && j >= 0 && j < GRID_SIZE) {
                    if (this.getGrid()[i][j] == c2) {
                        final String tempCandy = this.getGrid()[i][j].candyColor;

                        this.getGrid()[i][j].candyColor = this.getGrid()[c1.getSeed().x][c1.getSeed().y].candyColor;
                        this.getGrid()[c1.getSeed().x][c1.getSeed().y].candyColor = tempCandy;

                        return true;
                    }
                }
            }
        }

        return false;
    }

    private final List<List<Candy>> check(final Candy p) {
        final List<List<Candy>> inStreak = new ArrayList<List<Candy>>();

        int streak = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            if (this.getGrid()[i][p.getSeed().y] != null) {
                if (this.getGrid()[p.getSeed().x][p.getSeed().y].getColor() != this.getGrid()[i][p.getSeed().y]
                        .getColor()) {
                    streak = 0;
                    continue;
                }
            }

            streak++;
            if (streak == 3) {
                inStreak.add(new ArrayList<Candy>());
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[i - 2][p.getSeed().y]);
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[i - 1][p.getSeed().y]);
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[i][p.getSeed().y]);
            } else if (streak > 3) {
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[i][p.getSeed().y]);
            }
        }

        streak = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            if (this.getGrid()[p.getSeed().x][i] != null) {
                if (this.getGrid()[p.getSeed().x][p.getSeed().y].getColor() != this.getGrid()[p.getSeed().x][i]
                        .getColor()) {
                    streak = 0;
                    continue;
                }
            }

            streak++;
            if (streak == 3) {
                inStreak.add(new ArrayList<Candy>());
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[p.getSeed().x][i - 2]);
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[p.getSeed().x][i - 1]);
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[p.getSeed().x][i]);
            } else if (streak > 3) {
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[p.getSeed().x][i]);
            }
        }

        for (final List<Candy> arrayList : inStreak) {
            if (!arrayList.contains(this.getGrid()[p.getSeed().x][p.getSeed().y])) {
                inStreak.remove(arrayList);
            }
        }

        return inStreak;
    }

    private final void delete(final List<List<Candy>> points) {
        if (points != null) {
                for (List<Candy> list : points) {
                    for (final Candy candy : list) {
                        this.getGrid()[candy.getSeed().x][candy.getSeed().y].candyColor = "\u001B[37m";
                    }
            }
        }
    }

    private final void goDown() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j].getColor() == "\u001B[37m") {
                    if (grid[i][j].getSeed().x == 0) {
                        grid[i][j].candyColor = Candy.ANSI_COLORS[new Random().nextInt(Candy.ANSI_COLORS.length)];
                    } else {
                        swap(grid[i][j], grid[i - 1][j]);
                        goDown();
                    }
                }
            }
        }
    }

    public final Candy[][] getGrid() {
        return this.grid;
    }

    private static final class Candy {

        public static final String[] ANSI_COLORS = { "\u001B[31m", "\u001B[32m", "\u001B[34m", "\u001B[35m" };

        private String candyColor = ANSI_COLORS[new Random().nextInt(ANSI_COLORS.length)];
        private Point candySeed;

        public Candy(final Point candySeed) {
            this.setSeed(candySeed);
        }

        @Override
        public final String toString() {
            return String.format(">" + this.candyColor + "\u25A0" + "\u001B[0m" + "<");
        }

        public final String getColor() {
            return this.candyColor;
        }

        public final Point getSeed() {
            return this.candySeed;
        }

        public final void setSeed(final Point candySeed) {
            this.candySeed = Objects.requireNonNull(candySeed, "'candySeed' must not be null");
        }

    }

}
