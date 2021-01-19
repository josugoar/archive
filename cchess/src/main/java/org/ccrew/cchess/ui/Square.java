package org.ccrew.cchess.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class Square extends JPanel {

    private static final long serialVersionUID = 1L;

    Piece piece;

    public Square(int rank, int file, Piece piece) {
        setBackground((rank + file) % 2 == 0 ? new Color(251, 239, 221) : new Color(117, 98, 68));
        setPiece(piece);
        //  #665c46 black
        //  #b59767 white
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
             //   System.out.println(Square.this.piece);
                ChessBoard parent = (ChessBoard) getParent();
                if (parent.game.getClock().getWhiteRemainingSeconds()<=0 || parent.game.getClock().getBlackRemainingSeconds()<=0) {
                    return;
                } 
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
                       // System.out.println(move);
                        if (parent.game.getCurrentPlayer().doMove(move, true)) {
                            Square tempSquare = parent.selectedSquare;
                            parent.selectedSquare = null;
                            tempSquare.repaint();
                            tempSquare.revalidate();
                        }
                        parent.processFen(parent.game.moveStack.get(parent.index).getFen());
                    }
                }
                // for (ChessState state : parent.game.moveStack) {
                //    System.out.println(state.getFen());
                // }

            }
        });
    }

    String coordinates;

    public Square(int rank, int file) {
        this(rank, file, null);
        coordinates = String.format("%c%c", 'a' + file, '8' - rank);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (((ChessBoard) getParent()).selectedSquare == this) {
            if(piece!=null){
                g.drawImage(piece.pieceImage, 0, 0, getWidth(), getHeight(), this);
                return;
            }
        }
        if (piece != null) {
            g.drawImage(piece.pieceImage, (int) ((getWidth()-(getWidth() * 0.7))/2), (int) ((getHeight()-(getHeight() * 0.7))/2), (int) (getWidth() * 0.7), (int) (getHeight() * 0.7), this);
        }
    }
}
