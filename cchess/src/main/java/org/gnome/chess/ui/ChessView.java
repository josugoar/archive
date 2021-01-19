package org.gnome.chess.ui;

import static org.gnome.chess.util.Logging.warning;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class ChessView extends JPanel { // TODO Change to JComponent

    private static final long serialVersionUID = 1L;

    private int border = 6;
    private int squareSize;
    private int selectedSquareSize;
    private BufferedImage modelSurface;
    private BufferedImage selectedModelSurface;
    // private String loadedThemeName = "";

    private ChessScene scene;

    public ChessScene getScene() {
        return scene;
    }

    public void setScene(ChessScene scene) {
        this.scene = scene;
        // scene.changed.connect(sceneChangedCb);
        // queueDraw();
    }

    public double getBorderSize() {
        return squareSize / 2;
    }

    public ChessView(ChessScene scene) {
        setScene(scene);

        var clickController = new MouseInputAdapter() {

            public void mousePressed(MouseEvent e) {
                // only reacts to MouseEvent.BUTTON1
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // if (scene.game == null || scene.game.shouldShowPausedOverlay) {
                    // return;
                    // }

                    // If the game is over, disable selection of pieces
                    // if (scene.game.result != ChessResult.IN_PROGRESS) {
                    // return;
                    // }

                    int file = (int) Math.floor((e.getX() - 0.5 * getWidth() + squareSize * 4) / squareSize);
                    int rank = 7 - (int) Math.floor((e.getY() - 0.5 * getHeight() + squareSize * 4) / squareSize);

                    // if (scene.boardAngle == 180.0) {
                    // rank = 7 - rank;
                    // file = 7 - file;
                    // }

                    if (file < 0 || file >= 8 || rank < 0 || rank >= 8) {
                        return;
                    }

                    // scene.selectSquare(file, rank);
                }
            }
        };
        addMouseListener(clickController);

        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    public Dimension getPreferredSize() {
        int shortEdge = Integer.min(getParent().getWidth(), getParent().getHeight());

        squareSize = (int) Math.floor((shortEdge - 2 * border) / 9.0);
        var extra = squareSize * 0.1;
        if (extra < 3) {
            extra = 3;
        }
        selectedSquareSize = squareSize + 2 * (int) (extra + 0.5);

        return new Dimension(shortEdge, shortEdge);
    }

    private void renderPiece(Graphics c1, Graphics c2, String name, int offset) {
        // BufferedImage handle;

        try {
            // var stream = getClass().getClassLoader()
            // .getResourceAsStream("pieces/" + scene.themeName + "/" + name + ".png");
            // handle = ImageIO.read(stream);
        } catch (Exception e) {
            warning("Failed to load piece SVG: %s", e.getMessage());
            return;
        }

        try {
            // c1.drawImage(handle, squareSize * offset, 0, squareSize, squareSize, this);
            // c2.drawImage(handle, squareSize * offset, 0, selectedSquareSize,
            // selectedSquareSize, this);
        } catch (Exception e) {
            warning("Failed to render piece SVG: %s", e.getMessage());
        }
    }

    public void loadTheme(Graphics c) {
        /* Skip if already loaded */
        // if (scene.themeName == loadedThemeName && modelSurface != null && squareSize
        // == modelSurface.getHeight()) {
        // return;
        // }

        modelSurface = new BufferedImage(12 * squareSize, squareSize, BufferedImage.TYPE_INT_ARGB);
        selectedModelSurface = new BufferedImage(12 * selectedSquareSize, selectedSquareSize,
                BufferedImage.TYPE_INT_ARGB);
        // TODO
        var c1 = modelSurface.getGraphics();
        var c2 = selectedModelSurface.getGraphics();
        renderPiece(c1, c2, "whitePawn", 0);
        renderPiece(c1, c2, "whiteRook", 1);
        renderPiece(c1, c2, "whiteKnight", 2);
        renderPiece(c1, c2, "whiteBishop", 3);
        renderPiece(c1, c2, "whiteQueen", 4);
        renderPiece(c1, c2, "whiteKing", 5);
        renderPiece(c1, c2, "blackPawn", 6);
        renderPiece(c1, c2, "blackRook", 7);
        renderPiece(c1, c2, "blackKnight", 8);
        renderPiece(c1, c2, "blackBishop", 9);
        renderPiece(c1, c2, "blackQueen", 10);
        renderPiece(c1, c2, "blackKing", 11);

        // loadedThemeName = scene.themeName;
    }

}
