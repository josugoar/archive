package _DeustoCrush;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

/**
 * DeustoCrush
 * @author JoshGoA (ansi rectangle idea by nullx)
 */
public class DeustoCrush {

    private static final int GRID_N = 6;
    private static final int MIN_STREAK = 3;
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private int emptyMoves = 0;
    private int score = 0;
    protected Candy[][] grid;

    {
        this.randomize();
    }

    public static final void main(final String[] args) {
        new DeustoCrush().run();
    }

    @Override
    public final String toString() {
        String out = "";
        // Column number
        for (int i = 0; i < GRID_N; i++) {
            out += String.format("   %d", i);
        }
        out += "\n";
        for (int i = 0; i < GRID_N; i++) {
            // Row number
            out += i;
            for (int j = 0; j < GRID_N; j++) {
                // Candy
                out += String.format(" %s", this.getGrid()[i][j]);
            }
            out += "\n";
        }
        return out;
    }

    private final void run() {
        while (this.emptyMoves < 5) {
            // Print grid
            System.out.println(this);
            // Advance iter
            this.iterWrapper();
            // Read user input
            String[] input;
            try {
                System.out.println(String.format("Score: %d", this.score));
                System.out.print("Enter movement: ");
                input = br.readLine().split(" ");
                if (input.length != 2) {
                    throw new Exception();
                }
            } catch (final Exception e) {
                System.out.println("Invalid input");
                continue;
            } finally {
                System.out.println("");
            }
            // Store user input in integer array
            final int[][] points = new int[2][2];
            for (int i = 0; i < 2; i++) {
                points[i] = Stream.of(input[i].split("-")).mapToInt(Integer::parseInt).toArray();
            }
            // Swap input points
            if (!this.swap(this.getGrid()[points[0][0]][points[0][1]], this.getGrid()[points[1][0]][points[1][1]])) {
                System.out.println("Invalid neighboring points\n");
            }
        }
        System.out.println("Too many empty moves");
    }

    private final void randomize() {
        // Create new empty grid
        this.grid = new Candy[GRID_N][GRID_N];
        // Fill grid with randomly colored candies
        for (int i = 0; i < GRID_N; i++) {
            for (int j = 0; j < GRID_N; j++) {
                grid[i][j] = new Candy(new Point(i, j));
            }
        }
    }

    private final boolean swap(final Candy c1, final Candy c2) {
        // Range through four neighbors
        for (int i = c1.getSeed().x - 1; i <= c1.getSeed().x + 1; i++) {
            for (int j = c1.getSeed().y + Math.abs(c1.getSeed().x - i) - 1; j <= c1.getSeed().y + 1; j += 2) {
                // Check for array index out of bounds
                if (i >= 0 && i < GRID_N && j >= 0 && j < GRID_N) {
                    // Check for neighboring candies
                    if (this.getGrid()[i][j] == c2) {
                        // Swap colors but not seeds
                        final String tempColor = this.getGrid()[i][j].getColor();
                        this.getGrid()[i][j].setColor(c1.color);
                        c1.setColor(tempColor);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private final List<List<Candy>> check(final Candy c) {
        final List<List<Candy>> inStreak = new ArrayList<List<Candy>>();
        // Check horizontal streaks
        int streak = 0;
        for (int i = 0; i < GRID_N; i++) {
            // Reset streak
            if (this.getGrid()[c.getSeed().x][c.getSeed().y].getColor() != this.getGrid()[i][c.getSeed().y]
                    .getColor()) {
                streak = 0;
                continue;
            }
            // Advance streak
            streak++;
            if (streak == MIN_STREAK) {
                // Add new streak
                inStreak.add(new ArrayList<Candy>());
                for (int j = 0; j < MIN_STREAK; j++) {
                    inStreak.get(inStreak.size() - 1).add(this.getGrid()[i - j][c.getSeed().y]);
                }
            } else if (streak > MIN_STREAK) {
                // Add to existing streak
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[i][c.getSeed().y]);
            }
        }
        // Check vertical streaks
        streak = 0;
        for (int i = 0; i < GRID_N; i++) {
            // Reset streak
            if (this.getGrid()[c.getSeed().x][c.getSeed().y].getColor() != this.getGrid()[c.getSeed().x][i]
                    .getColor()) {
                streak = 0;
                continue;
            }
            // Advance streak
            streak++;
            if (streak == MIN_STREAK) {
                // Add new streak
                inStreak.add(new ArrayList<Candy>());
                for (int j = 0; j < MIN_STREAK; j++) {
                    inStreak.get(inStreak.size() - 1).add(this.getGrid()[c.getSeed().x][i - j]);
                }
            } else if (streak > MIN_STREAK) {
                // Add to existing streak
                inStreak.get(inStreak.size() - 1).add(this.getGrid()[c.getSeed().x][i]);
            }
        }
        // Use fori instead of foreach to avoid ConcurrentModificationException
        for (int i = 0; i < inStreak.size(); i++) {
            // Check streaks containing candy
            if (!inStreak.get(i).contains(this.getGrid()[c.getSeed().x][c.getSeed().y])) {
                inStreak.remove(inStreak.get(i));
            }
        }
        return inStreak;
    }

    private final void calcPoints(final List<List<Candy>> inStreak) {
        // Check for existing streak
        if (inStreak != null) {
            // Use fori instead of foreach to avoid ConcurrentModificationException
            for (int i = 0; i < inStreak.size(); i++) {
                for (int j = 0; j < inStreak.get(i).size(); j++) {
                    this.score += 100;
                }
            }
        } else {
            this.emptyMoves++;
        }
    }

    private final boolean delete(final List<List<Candy>> inStreak) {
        // Deleted flag
        boolean deleted = false;
        // Check for existing streak
        if (inStreak != null) {
            // Use fori instead of foreach to avoid ConcurrentModificationException
            for (int i = 0; i < inStreak.size(); i++) {
                for (int j = 0; j < inStreak.get(i).size(); j++) {
                    // Remove individual candy color
                    inStreak.get(i).get(j).setColor(null);
                    deleted = true;
                }
            }
        }
        return deleted;
    }

    private final void advance() {
        // Range through each candy
        for (int i = 0; i < GRID_N; i++) {
            for (int j = 0; j < GRID_N; j++) {
                // Check for deleted candy
                if (this.getGrid()[i][j].getColor() == null) {
                    // Redefine leading row candies
                    if (this.getGrid()[i][j].getSeed().x == 0) {
                        this.getGrid()[i][j] = new Candy(this.getGrid()[i][j].getSeed());
                    } else {
                        // Swap candy with top candy
                        swap(this.getGrid()[i][j], this.getGrid()[i - 1][j]);
                        // Recursively call method until convergence
                        advance();
                    }
                }
            }
        }
    }

    private final void iterWrapper() {
        boolean deleted = false;
        // Wrap check, delete and advance
        for (int i = 0; i < GRID_N; i++) {
            for (int j = 0; j < GRID_N; j++) {
                // Check for streak and delete it
                if (!deleted) {
                    final List<List<Candy>> inStreak = this.check(this.getGrid()[i][j]);
                    this.calcPoints(inStreak);
                    deleted = this.delete(inStreak);
                }
            }
        }
        // Advance deleted candies
        this.advance();
        // Delete new streaks if they appear
        if (deleted) {
            System.out.println(this);
            this.iterWrapper();
        }
    }

    public final Candy[][] getGrid() {
        return this.grid;
    }

    private static final class Candy {

        // Color strings
        public static final String[] ANSI_COLORS = { "\u001B[31m", "\u001B[32m", "\u001B[34m", "\u001B[35m" };

        // Initialize color with random string
        private String color = ANSI_COLORS[new Random().nextInt(ANSI_COLORS.length)];
        private Point seed;

        public Candy(final Point candySeed) {
            this.setSeed(candySeed);
        }

        @Override
        public final String toString() {
            // Return formatted candy
            return String.format(">" + this.color + "\u25A0" + "\u001B[0m" + "<");
        }

        public final String getColor() {
            return this.color;
        }

        public final void setColor(final String color) {
            this.color = color;
        }

        public final Point getSeed() {
            return this.seed;
        }

        public final void setSeed(final Point seed) {
            this.seed = Objects.requireNonNull(seed, "'seed' must not be null");
        }

    }

}
