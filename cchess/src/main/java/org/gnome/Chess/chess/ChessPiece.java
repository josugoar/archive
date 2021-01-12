package org.gnome.Chess.chess;

import java.util.EventObject;

import javax.lang.model.type.NullType;

import org.gnome.Chess.util.Signal;

public class ChessPiece {

    public static class ChessPieceEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        public ChessPieceEvent(ChessPiece source) {
            super(source);
        }

        @Override
        public ChessPiece getSource() {
            return (ChessPiece) super.getSource();
        }

    }

    public ChessPlayer player;
    public PieceType type;

    public Signal<ChessPieceEvent, NullType> moved = new Signal<>();
    public Signal<ChessPieceEvent, NullType> promoted = new Signal<>();
    public Signal<ChessPieceEvent, NullType> died = new Signal<>();

    public Color getColor() {
        return player.color;
    }

    public char getSymbol() {
        char c = ' ';
        switch (type) {
            case PAWN:
                c = 'p';
                break;
            case ROOK:
                c = 'r';
                break;
            case KNIGHT:
                c = 'n';
                break;
            case BISHOP:
                c = 'b';
                break;
            case QUEEN:
                c = 'q';
                break;
            case KING:
                c = 'k';
                break;
        }
        if (player.color == Color.WHITE) {
            c = Character.toUpperCase(c);
        }
        return c;
    }

    public ChessPiece(ChessPlayer player, PieceType type) {
        this.player = player;
        this.type = type;
    }

}
