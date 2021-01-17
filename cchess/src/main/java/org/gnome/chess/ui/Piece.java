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

    public Piece(PieceType pieceType, Color pieceColor){
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        String pieceName = "";

        switch (pieceColor) {
            case WHITE:
                pieceName += "white";
                break;
            case BLACK:
                pieceName += "black";
                break;
        }
        switch (pieceType) {
            case PAWN:
                pieceName += "Pawn";
                break;        
            case ROOK:
                pieceName += "Rook";
                break;
            case KNIGHT:
                pieceName += "Knight";
                break;
            case BISHOP:
                pieceName += "Bishop";
                break;
            case QUEEN:
                pieceName += "Queen";
                break;
            case KING:
                pieceName += "King";
                break;
        }

        try {
            pieceImage = ImageIO.read(getClass().getClassLoader().getResource("pieces/simple/"+pieceName+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Piece createWhite(PieceType type){
        return new Piece(type, Color.WHITE);
    }

    public static Piece createBlack(PieceType type){
        return new Piece(type, Color.BLACK);
    }
    
}
