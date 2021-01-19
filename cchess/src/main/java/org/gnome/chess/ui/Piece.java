package org.gnome.chess.ui;

import org.gnome.chess.lib.Color;
import org.gnome.chess.lib.PieceType;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Piece {

    PieceType pieceType;
    Color pieceColor;
    BufferedImage pieceImage;

    public Piece(PieceType pieceType, Color pieceColor) {
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        String pieceName = "";

        switch (pieceType) {
            case PAWN:
                pieceName = Config.getPawn(pieceColor == Color.WHITE);
                break;
            case ROOK:
                pieceName = Config.getRook(pieceColor == Color.WHITE);
                break;
            case KNIGHT:
                pieceName = Config.getKnight(pieceColor == Color.WHITE);
                break;
            case BISHOP:
                pieceName = Config.getBishop(pieceColor == Color.WHITE);
                break;
            case QUEEN:
                pieceName = Config.getQueen(pieceColor == Color.WHITE);
                break;
            case KING:
                pieceName = Config.getKing(pieceColor == Color.WHITE);
                break;
        }

        try {
            pieceImage = ImageIO.read(getClass().getClassLoader().getResource(pieceName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Piece createWhite(PieceType type) {
        return new Piece(type, Color.WHITE);
    }

    public static Piece createBlack(PieceType type) {
        return new Piece(type, Color.BLACK);
    }

}
