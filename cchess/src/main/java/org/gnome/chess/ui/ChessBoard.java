package org.gnome.chess.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gnome.chess.lib.ChessGame;
import org.gnome.chess.lib.ChessGame.MovedSource;
import org.gnome.chess.lib.ChessPiece;
import org.gnome.chess.lib.PGNError;
import org.gnome.chess.lib.PieceType;

public class ChessBoard extends JPanel {

    private static final long serialVersionUID = 1L;

    Square[][] board = new Square[8][8];
    ChessGame game = null;
    Square selectedSquare = null;

    public ChessBoard() {

        try {
            game = new ChessGame();
            game.start();
        } catch (PGNError e) {
            e.printStackTrace();
        }
        setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = new Square(i, j);
                board[i][j] = square;
                add(square);
            }
        }
        processFen(ChessGame.STANDARD_SETUP);
        game.moved.connect((MovedSource s) -> {
            processFen(s.getSource().moveStack.get(0).getFen());
            return Void.TYPE;
        });
    }

    public int index = 0;

    public void setGame(ChessGame game) {
        this.game = game;
        game.start();
        processFen(game.getCurrentState().getFen());
        game.moved.connect((MovedSource s) -> {
            processFen(s.getSource().moveStack.get(0).getFen());
            return Void.TYPE;
        });
    }

    public void recursiveProcessFen(Iterator<ChessPiece> piece, int i) {
        if (piece.hasNext()) {
            ChessPiece chessPiece = piece.next();
            int rank = 7 - (i / 8);
            int file = 7 - (i % 8);
            board[rank][file].setPiece(null);
            if (chessPiece != null) {
                board[rank][file].setPiece(new Piece(chessPiece.type, chessPiece.getColor()));
            }
            board[rank][file].repaint();
            board[rank][file].revalidate();
            recursiveProcessFen(piece, i + 1);
        }
    }

    public void processFen(String fen) {

        try {
            if (game == null) {
                game = new ChessGame(fen);
                game.start();
            }
        } catch (PGNError e) {
            e.printStackTrace();
        }

        ChessPiece[] unprocessedBoard = game.moveStack.get(index).board;
        Iterator<ChessPiece> list = Arrays.asList(unprocessedBoard).iterator();
        recursiveProcessFen(list, 0);
    }

    public void startingState() {

        board[6][0].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][1].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][2].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][3].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][4].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][5].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][6].setPiece(Piece.createWhite(PieceType.PAWN));
        board[6][7].setPiece(Piece.createWhite(PieceType.PAWN));

        board[1][0].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][1].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][2].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][3].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][4].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][5].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][6].setPiece(Piece.createBlack(PieceType.PAWN));
        board[1][7].setPiece(Piece.createBlack(PieceType.PAWN));

        board[0][0].setPiece(Piece.createBlack(PieceType.ROOK));
        board[0][1].setPiece(Piece.createBlack(PieceType.KNIGHT));
        board[0][2].setPiece(Piece.createBlack(PieceType.BISHOP));
        board[0][3].setPiece(Piece.createBlack(PieceType.QUEEN));
        board[0][4].setPiece(Piece.createBlack(PieceType.KING));
        board[0][5].setPiece(Piece.createBlack(PieceType.BISHOP));
        board[0][6].setPiece(Piece.createBlack(PieceType.KNIGHT));
        board[0][7].setPiece(Piece.createBlack(PieceType.ROOK));

        board[7][0].setPiece(Piece.createWhite(PieceType.ROOK));
        board[7][1].setPiece(Piece.createWhite(PieceType.KNIGHT));
        board[7][2].setPiece(Piece.createWhite(PieceType.BISHOP));
        board[7][3].setPiece(Piece.createWhite(PieceType.QUEEN));
        board[7][4].setPiece(Piece.createWhite(PieceType.KING));
        board[7][5].setPiece(Piece.createWhite(PieceType.BISHOP));
        board[7][6].setPiece(Piece.createWhite(PieceType.KNIGHT));
        board[7][7].setPiece(Piece.createWhite(PieceType.ROOK));

    }

    public static void main(String[] args) {

        ChessBoard board = new ChessBoard();
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);
        frame.add(board);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        int width = parent.getWidth();
        int height = parent.getHeight();
        int size = Math.min(width, height);
        return new Dimension(size, size);
    }

}
