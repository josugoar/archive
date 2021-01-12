package org.gnome.Chess.chess;

import static org.gnome.Chess.util.AssertNotReached.assertNotReached;

public enum ClockType {

    SIMPLE, FISCHER, BRONSTEIN, INVALID;

    @Override
    public String toString() {
        switch (this) {
            case SIMPLE:
                return "simple";
            case FISCHER:
                return "fischer";
            case BRONSTEIN:
                return "bronstein";
            default:
                throw assertNotReached();
        }
    }

    public static ClockType stringToEnum(String s) {
        switch (s) {
            case "simple":
                return SIMPLE;
            case "fischer":
                return FISCHER;
            case "bronstein":
                return BRONSTEIN;
            default:
                return INVALID;
        }
    }

}
