package org.gnome.chess.ui;

import static org.gnome.chess.util.Logging.warning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import org.gnome.chess.lib.ChessResult;

public class ChessView extends JPanel { // TODO Change to JComponent

    private static final long serialVersionUID = 1L;

    private int border = 6;
    private int squareSize;
    private int selectedSquareSize;
    private BufferedImage modelSurface;
    private BufferedImage selectedModelSurface;
    private String loadedThemeName = "";

    private ChessScene scene;

    public ChessScene getScene() {
        return scene;
    }

    public void setScene(ChessScene scene) {
        this.scene = scene;
        scene.changed.connect(sceneChangedCb);
        queueDraw();
    }

    private double getBorderSize() {
        return squareSize / 2;
    }

    public ChessView(ChessScene scene) {
        setScene(scene);

        var clickController = new MouseInputAdapter() {

            public void mousePressed(MouseEvent e) {
                // only reacts to MouseEvent.BUTTON1
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (scene.game == null || scene.game.shouldShowPausedOverlay) {
                        return;
                    }

                    // If the game is over, disable selection of pieces
                    if (scene.game.result != ChessResult.IN_PROGRESS) {
                        return;
                    }

                    int file = (int) Math.floor((e.getX() - 0.5 * getWidth() + squareSize * 4) / squareSize);
                    int rank = 7 - (int) Math.floor((e.getY() - 0.5 * getHeight() + squareSize * 4) / squareSize);

                    if (scene.boardAngle == 180.0) {
                        rank = 7 - rank;
                        file = 7 - file;
                    }

                    if (file < 0 || file >= 8 || rank < 0 || rank >= 8) {
                        return;
                    }

                    scene.selectSquare(file, rank);
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
        BufferedImage handle;

        try {
            var stream = getClass().getClassLoader()
                    .getResourceAsStream("pieces/" + scene.themeName + "/" + name + ".png");
            handle = ImageIO.read(stream);
        } catch (Exception e) {
            warning("Failed to load piece SVG: %s", e.getMessage());
            return;
        }

        try {
            c1.drawImage(handle, squareSize * offset, 0, squareSize, squareSize, this);
            c2.drawImage(handle, squareSize * offset, 0, selectedSquareSize, selectedSquareSize, this);
        } catch (Exception e) {
            warning("Failed to render piece SVG: %s", e.getMessage());
        }
    }

    private void loadTheme(Graphics c) {
        /* Skip if already loaded */
        if (scene.themeName == loadedThemeName && modelSurface != null && squareSize == modelSurface.getHeight()) {
            return;
        }

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

        loadedThemeName = scene.themeName;
    }

    @Override
    public void paintComponent (Graphics c) {
        loadTheme(c);

        c.translate (getWidth() / 2, getHeight() / 2);
        c.rotate (Math.PI * scene.boardAngle / 180.0);

        int boardSize = (int) Math.ceil (squareSize * 4 + getBorderSize());
        c.setColor(new Color((int)(0x2e/255.0), (int)(0x34/255.0), (int)(0x36/255.0)));
        c.fillRect(-boardSize, -boardSize, boardSize * 2, boardSize * 2);

        for (int file = 0; file < 8; file++){
            for (int rank = 0; rank < 8; rank++){
                int x = (int) ((file - 4) * squareSize);
                int y = (int) ((3 - rank) * squareSize);

                if((file + rank) % 2 == 0){
                    c.setColor(new Color((int)(0xba/255.0), (int)(0xbd/255.0), (int)(0xb6/255.0)));
                }else{
                    c.setColor(new Color((int)(0xee/255.0), (int)(0xee/255.0), (int)(0xec/255.0)));
                }
                c.fillRect(x, y, squareSize, squareSize);
            }
        }

        if (scene.showNumbering) {
            /* Files are centered individiual glyph width and combined glyph height,
             * ranks are centered on individual glyph widths and heights */

            c.setColor(new Color((int)(0x88/255.0), (int)(0x8a/255.0), (int)(0x85/255.0)));
            c.setFont(new Font( Font.SANS_SERIF, Font.BOLD, (int) (getBorderSize() * 0.6)));

            c.drawString(iterator, x, y);
            Cairo.TextExtents extents;
            c.text_extents ("abcdefgh", out extents);
            double yOffset = (squareSize / 2 - extents.height) / 2 + extents.height + extents.y_bearing;
            double top = -(squareSize * 4 + yOffset);
            double bottom = squareSize * 4 + getBorderSize() - yOffset;

            double fileOffset = -(squareSize * 3.5);
            double rankOffset = -(squareSize * 3.5);

            String[] files;
            String[] ranks;

            Cairo.Matrix matrix = c.get_matrix ();

            if (scene.boardAngle == 180.0)
            {
                files = { "h", "g", "f", "e", "d", "c", "b", "a" };
                ranks = { "1", "2", "3", "4", "5", "6", "7", "8" };

                matrix.scale (-1, -1);
            }
            else
            {
                files = { "a", "b", "c", "d", "e", "f", "g", "h" };
                ranks = { "8", "7", "6", "5", "4", "3", "2", "1" };
            }

            c.save ();
            c.set_matrix (matrix);

            for (int i = 0; i < 8; i++)
            {
                c.text_extents (ranks[i], out extents);

                /* Black file */
                c.save ();
                c.move_to (file_offset - extents.width / 2, top);
                c.show_text (files[i]);
                c.restore ();

                /* White file */
                c.save ();
                c.move_to (file_offset - extents.width / 2, bottom);
                c.show_text (files[i]);
                c.restore ();

                c.text_extents (ranks[i], out extents);
                y_offset = -(extents.y_bearing + extents.height / 2);

                /* Left rank */
                c.save ();
                c.move_to (-((double) square_size * 4 + border_size - (border_size - extents.width) / 2), rank_offset + y_offset);
                c.show_text (ranks[i]);
                c.restore ();

                /* Right rank */
                c.save ();
                c.move_to ((double) square_size * 4 + (border_size - extents.width) / 2, rank_offset + y_offset);
                c.show_text (ranks[i]);
                c.restore ();

                file_offset += square_size;
                rank_offset += square_size;
            }

            c.restore ();
        }

        /* Draw pause overlay */
        if (scene.game.should_show_paused_overlay)
        {
            c.rotate (Math.PI * scene.board_angle / 180.0);
            draw_paused_overlay (c);
            return;
        }

        /* Draw the pieces */
        foreach (var model in scene.pieces)
        {
            c.save ();
            c.translate ((model.x - 4) * square_size, (3 - model.y) * square_size);
            c.translate (square_size / 2, square_size / 2);
            c.rotate (-Math.PI * scene.board_angle / 180.0);

            draw_piece (c,
                        model.is_selected ? selected_model_surface : model_surface,
						model.is_selected ? selected_square_size : square_size,
                        model.piece, model.under_threat && scene.show_move_hints ? 0.8 : 1.0);

            c.restore ();
        }

        /* Draw shadow piece on squares that can be moved to */
        for (int rank = 0; rank < 8; rank++)
        {
            for (int file = 0; file < 8; file++)
            {
                if (scene.show_move_hints && scene.can_move (rank, file))
                {
                    c.save ();
                    c.translate ((file - 4) * square_size, (3 - rank) * square_size);
                    c.translate (square_size / 2, square_size / 2);
                    c.rotate (-Math.PI * scene.board_angle / 180.0);

                    draw_piece (c, model_surface, square_size, scene.get_selected_piece (), 0.1);

                    c.restore ();
                }
            }
        }
    }

}
