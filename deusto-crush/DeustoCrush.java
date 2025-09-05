package _DeustoCrush;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * DeustoCrush
 *
 * @author JoshGoA (ansi rectangle idea by nullx)
 */
public class DeustoCrush extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private static final int GRID_N = 6;
    private static final int MIN_STREAK = 3;
    private static final int DELAY = 500;

    private final JPanel panel = new JPanel(new GridLayout(GRID_N, GRID_N, 0, 0));

    private int emptyMoves = 0;
    private int score = 0;
    private Candy[][] grid;
    private Point point = null;

    {
        // Randomize grid on start
        this.randomize();
    }

    public static final void main(final String[] args) {
        // Invoke Runnable
        EventQueue.invokeLater(new DeustoCrush());
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
                out += String.format(" %s", this.grid[i][j]);
            }
            out += "\n";
        }
        return out;
    }

    /**
     * Randomize grid
     */
    private final void randomize() {
        // Create new empty grid
        this.grid = new Candy[GRID_N][GRID_N];
        // Fill grid with randomly colored candies
        for (int i = 0; i < GRID_N; i++) {
            for (int j = 0; j < GRID_N; j++) {
                final Candy candy = new Candy(new Point(i, j));
                candy.addMouseListener(candy.new CandyListener());
                grid[i][j] = candy;
                this.panel.add(candy);
            }
        }
    }

    @Override
    public final void run() {
        this.panel.setPreferredSize(new Dimension(250, 250));
        this.setContentPane(panel);
        this.pack();
        this.setTitle("DeustoCrush");
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.iterWrapper();
    }

    /**
     * Swap two Candy
     *
     * @param c1
     * @param c2
     */
    private final void swap(final Candy c1, final Candy c2) {
        // Range through four neighbors
        for (int i = c1.getSeed().x - 1; i <= c1.getSeed().x + 1; i++) {
            for (int j = c1.getSeed().y + Math.abs(c1.getSeed().x - i) - 1; j <= c1.getSeed().y + 1; j += 2) {
                // Check for array index out of bounds
                if (i >= 0 && i < GRID_N && j >= 0 && j < GRID_N) {
                    // Check for neighboring candies
                    if (this.grid[i][j] == c2) {
                        // Swap colors but not seeds
                        final String tempColor = this.grid[i][j].getColor();
                        this.grid[i][j].setColor(c1.color);
                        c1.setColor(tempColor);
                    }
                }
            }
        }
    }

    /**
     * Check for Candy in streak
     *
     * @param c
     * @return
     */
    private final List<List<Candy>> check(final Candy c) {
        final List<List<Candy>> inStreak = new ArrayList<List<Candy>>();
        // Check horizontal streaks
        int streak = 0;
        for (int i = 0; i < GRID_N; i++) {
            // Reset streak
            if (this.grid[c.getSeed().x][c.getSeed().y].getColor() != this.grid[i][c.getSeed().y].getColor()) {
                streak = 0;
                continue;
            }
            // Advance streak
            streak++;
            if (streak == MIN_STREAK) {
                // Add new streak
                inStreak.add(new ArrayList<Candy>());
                for (int j = 0; j < MIN_STREAK; j++) {
                    inStreak.get(inStreak.size() - 1).add(this.grid[i - j][c.getSeed().y]);
                }
            } else if (streak > MIN_STREAK) {
                // Add to existing streak
                inStreak.get(inStreak.size() - 1).add(this.grid[i][c.getSeed().y]);
            }
        }
        // Check vertical streaks
        streak = 0;
        for (int i = 0; i < GRID_N; i++) {
            // Reset streak
            if (this.grid[c.getSeed().x][c.getSeed().y].getColor() != this.grid[c.getSeed().x][i].getColor()) {
                streak = 0;
                continue;
            }
            // Advance streak
            streak++;
            if (streak == MIN_STREAK) {
                // Add new streak
                inStreak.add(new ArrayList<Candy>());
                for (int j = 0; j < MIN_STREAK; j++) {
                    inStreak.get(inStreak.size() - 1).add(this.grid[c.getSeed().x][i - j]);
                }
            } else if (streak > MIN_STREAK) {
                // Add to existing streak
                inStreak.get(inStreak.size() - 1).add(this.grid[c.getSeed().x][i]);
            }
        }
        // Use fori instead of foreach to avoid ConcurrentModificationException
        for (int i = 0; i < inStreak.size(); i++) {
            // Check streaks containing candy
            if (!inStreak.get(i).contains(this.grid[c.getSeed().x][c.getSeed().y])) {
                inStreak.remove(inStreak.get(i));
            }
        }
        return inStreak;
    }

    /**
     * Calculate streak points
     *
     * @param inStreak
     */
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

    /**
     * Delete Candy in streak
     *
     * @param inStreak
     * @return
     */
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

    /**
     * Advance Candy vertically
     */
    private final void advance() {
        // Range through each candy
        for (int i = 0; i < GRID_N; i++) {
            for (int j = 0; j < GRID_N; j++) {
                // Check for deleted candy
                if (this.grid[i][j].getColor() == null) {
                    // Redefine leading row candies
                    if (this.grid[i][j].getSeed().x == 0) {
                        this.grid[i][j].setColor(Candy.ANSI_COLORS[new Random().nextInt(Candy.ANSI_COLORS.length)]);
                    } else {
                        // Swap candy with top candy
                        swap(this.grid[i][j], this.grid[i - 1][j]);
                        // Recursively call method until convergence
                        advance();
                    }
                }
            }
        }
    }

    /**
     * Repaint all Candy
     */
    private final void paint() {
        for (final Candy[] candies : this.grid) {
            for (final Candy candy : candies) {
                candy.repaint();
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
                    final List<List<Candy>> inStreak = this.check(this.grid[i][j]);
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
            System.out.println("Score: " + this.score);
            // Set delay for repaint
            new Timer(DeustoCrush.DELAY, e -> {
                this.paint();
                this.iterWrapper();
                ((Timer) e.getSource()).stop();
            }).start();
        }
    }

    public final Candy[][] getGrid() {
        return this.grid;
    }

    public final Point getPoint() {
        return this.point;
    }

    public final void setPoint(final Point point) {
        this.point = point;
    }

    /**
     * Candy
     */
    private static final class Candy extends JPanel {

        private static final long serialVersionUID = 1L;

        // Color strings
        public static final String[] ANSI_COLORS = { "\u001B[31m", "\u001B[32m", "\u001B[34m", "\u001B[35m" };

        // Initialize color with random string
        private String color = ANSI_COLORS[new Random().nextInt(ANSI_COLORS.length)];
        private Point seed;

        {
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }

        public Candy(final Point candySeed) {
            this.setSeed(candySeed);
        }

        @Override
        public final void paintComponent(final Graphics g) {
            Color c;
            // Paint Cell depending on Color
            if (this.color == null) {
                c = Color.WHITE;
            } else {
                switch (this.color) {
                    case "\u001B[31m":
                        c = Color.RED;
                        break;
                    case "\u001B[32m":
                        c = Color.GREEN;
                        break;
                    case "\u001B[34m":
                        c = Color.BLUE;
                        break;
                    default:
                        c = Color.MAGENTA;
                }
            }
            g.setColor(c);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
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

        /**
         * Candy Listener
         */
        private final class CandyListener extends MouseAdapter {

            @Override
            public void mousePressed(final MouseEvent e) throws NullPointerException {
                // Get frame
                final DeustoCrush frame = (DeustoCrush) SwingUtilities.getWindowAncestor(Candy.this);
                // Check if Candy is selected
                if (frame.getPoint() == null) {
                    frame.setPoint(Candy.this.getSeed());
                } else {
                    // Check for too many empty moves
                    if (frame.emptyMoves >= 5) {
                        System.out.println("Too many empty moves...");
                        try {
                            Thread.sleep(1000000);
                        } catch (InterruptedException e1) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    // Swap Candy
                    frame.swap(frame.grid[frame.getPoint().x][frame.getPoint().y], Candy.this);
                    // Increase empty moves if no Candy have been deleted
                    if (!frame.delete(frame.check(frame.grid[frame.getPoint().x][frame.getPoint().y]))
                            && !frame.delete(frame.check(Candy.this))) {
                        frame.emptyMoves++;
                    }
                    // Repaint Candy
                    frame.paint();
                    // Reset selection
                    frame.setPoint(null);
                    // Check for more streaks
                    frame.iterWrapper();
                }
            }

        }

    }

}
