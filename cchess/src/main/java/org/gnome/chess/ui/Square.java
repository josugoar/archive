package org.gnome.chess.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class Square extends JPanel {

    private static final long serialVersionUID = 1L;

    Piece piece;

    public Square(int rank, int file, Piece piece) {
        setBackground((rank + file) % 2 == 0 ? Color.WHITE : Color.GRAY);
        setPiece(piece);
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(Square.this.piece);
                ChessBoard parent = (ChessBoard) getParent();
                if (parent.index != 0) {
                    return;
                }
                if (parent.selectedSquare == Square.this) {
                    Square tempSquare = parent.selectedSquare;
                    parent.selectedSquare = null;
                    tempSquare.repaint();
                    tempSquare.revalidate();
                    repaint();
                    revalidate();
                    return;
                }
                if (parent.selectedSquare == null) {
                    if (Square.this.piece != null) {
                        if (parent.game.getCurrentPlayer().color == Square.this.piece.pieceColor) {
                            parent.selectedSquare = Square.this;
                            repaint();
                            revalidate();
                        }
                    }
                } else {
                    if (Square.this.piece != null
                            && Square.this.piece.pieceColor == parent.selectedSquare.piece.pieceColor) {
                        Square tempSquare = parent.selectedSquare;
                        parent.selectedSquare = Square.this;
                        tempSquare.repaint();
                        tempSquare.revalidate();
                        repaint();
                        revalidate();
                    } else {
                        String move = String.format("%s-%s", parent.selectedSquare.coordinates,
                                Square.this.coordinates);
                        System.out.println(move);
                        if (parent.game.getCurrentPlayer().doMove(move, true)) {
                            Square tempSquare = parent.selectedSquare;
                            parent.selectedSquare = null;
                            tempSquare.repaint();
                            tempSquare.revalidate();
                        }
                        parent.processFen(parent.game.moveStack.get(parent.index).getFen());
                    }
                }

            }
        });
    }

    String coordinates;

    public Square(int rank, int file) {
        this(rank, file, null);
        coordinates = String.format("%c%c", 'a' + file, '1' + rank);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (piece != null) {
            g.drawImage(piece.pieceImage, (int) ((getWidth()-(getWidth() * 0.7))/2), (int) ((getHeight()-(getHeight() * 0.7))/2), (int) (getWidth() * 0.7), (int) (getHeight() * 0.7), this);
        }
        if (((ChessBoard) getParent()).selectedSquare == this) {
            g.drawImage(piece.pieceImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
