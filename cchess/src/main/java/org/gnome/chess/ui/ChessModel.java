package org.gnome.chess.ui;

import org.gnome.chess.chess.ChessPiece;

public class ChessModel {

    public ChessPiece piece;
    public double x;
    public double y;
    public double targetX;
    public double targetY;
    public boolean underThreat;
    public boolean isSelected;

    public boolean getMoving() {
        return x != targetX || y != targetY;
    }

    public ChessModel(ChessPiece piece, double x, double y) {
        this.piece = piece;
        this.x = this.targetX = x;
        this.y = this.targetY = y;
    }

    public boolean moveTo(double x, double y) {
        if (targetX == x && targetY == y) {
            return false;
        }

        targetX = x;
        targetY = y;

        return true;
    }

    public boolean animate(double timestep) {
        if (!getMoving()) {
            return false;
        }

        x = updatePosition(timestep, x, targetX);
        y = updatePosition(timestep, y, targetY);
        return true;
    }

    private double updatePosition(double timestep, double value, double target) {
        var distance = Math.abs(target - value);
        var step = timestep * 4.0;

        if (step > distance) {
            step = distance;
        }

        if (target > value) {
            return value + step;
        } else {
            return value - step;
        }
    }

}
