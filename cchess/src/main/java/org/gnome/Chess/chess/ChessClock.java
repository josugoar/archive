package org.gnome.Chess.chess;

import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import javax.lang.model.type.NullType;

import org.gnome.Chess.util.Signal;

// TODO: private Timer? timer; private uint tick_timeout_id = 0;

public class ChessClock {

    public static class ChessClockEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        public ChessClockEvent(ChessClock source) {
            super(source);
        }

        @Override
        public ChessClock getSource() {
            return (ChessClock) super.getSource();
        }

    }

    private int whiteInitialSeconds;
    private int blackInitialSeconds;

    private int whiteSecondsUsed = 0;
    private int blackSecondsUsed = 0;

    private int whitePrevMoveSeconds = 0;
    private int blackPrevMoveSeconds = 0;

    private int whiteExtraSeconds = 0;
    private int blackExtraSeconds = 0;

    private int extraSeconds;

    public int getExtraSeconds() {
        return extraSeconds;
    }

    public void setExtraSeconds(int extraSeconds) {
        this.extraSeconds = extraSeconds;
    }

    {
        setExtraSeconds(0);
    }

    public int getWhiteRemainingSeconds() {
        return whiteInitialSeconds + whiteExtraSeconds - whiteSecondsUsed;
    }

    public int getBlackRemainingSeconds() {
        return blackInitialSeconds + blackExtraSeconds - blackSecondsUsed;
    }

    private ClockType clockType;

    public ClockType getClockType() {
        return clockType;
    }

    public void setClockType(ClockType clockType) {
        this.clockType = clockType;
    }

    {
        setClockType(ClockType.SIMPLE);
    }

    private Color activeColor = Color.WHITE;

    public Color getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(Color activeColor) {
        if (activeColor == this.activeColor) {
            return;
        }

        stop();
        this.activeColor = activeColor;

        // This is a move switch
        // Update the clocks for Fischer and Bronstein mode
        updateExtraSeconds();
        updatePrevMoveTime();

        start();
    }

    public Signal<ChessClockEvent, NullType> tick = new Signal<>();
    public Signal<ChessClockEvent, NullType> expired = new Signal<>();

    private boolean isActive = false;

    public ChessClock(int whiteInitialSeconds, int blackInitialSeconds) {
        this.whiteInitialSeconds = whiteInitialSeconds;
        this.blackInitialSeconds = blackInitialSeconds;
    }

    public void start() {
        if (isActive) {
            return;
        }

        isActive = true;

        watchTimer();
    }

    private Supplier<TimerTask> tickCb = () -> new TimerTask() {

        @Override
        public void run() {
            if (activeColor == Color.WHITE) {
                whiteSecondsUsed++;
            } else {
                blackSecondsUsed++;
            }

            tick.emit(new ChessClockEvent(ChessClock.this));

            if (whiteSecondsUsed >= whiteInitialSeconds + whiteExtraSeconds
                    || blackSecondsUsed >= blackInitialSeconds + blackExtraSeconds) {
                stop();
                expired.emit(new ChessClockEvent(ChessClock.this));
            }
        }

    };

    public void stop() {
        if (!isActive) {
            return;
        }

        stopWatchingTimer();
        isActive = false;
    }

    public void pause() {
        if (!isActive) {
            return;
        }

        stopWatchingTimer();
        isActive = false;
    }

    public void unpause() {
        if (isActive) {
            return;
        }

        watchTimer();
        isActive = true;
    }

    private Timer timeout = new Timer(true);

    private void watchTimer() {
        /* Wake up each second */
        timeout.scheduleAtFixedRate(tickCb.get(), 0, 1000);
    }

    private void stopWatchingTimer() {
        timeout.cancel();
        timeout = new Timer(true);
    }

    private void updatePrevMoveTime() {
        if (activeColor == Color.WHITE) {
            blackPrevMoveSeconds = blackSecondsUsed;
        } else {
            whitePrevMoveSeconds = whiteSecondsUsed;
        }
    }

    private void updateExtraSeconds() {
        int whiteMoveUsed = 0;
        int blackMoveUsed = 0;
        switch (clockType) {
            case FISCHER:
                if (activeColor == Color.WHITE) {
                    whiteExtraSeconds += extraSeconds;
                } else {
                    blackExtraSeconds += extraSeconds;
                }
                break;
            case BRONSTEIN:
                whiteMoveUsed = whiteSecondsUsed - whitePrevMoveSeconds;
                blackMoveUsed = blackSecondsUsed - blackPrevMoveSeconds;
                if (activeColor != Color.WHITE) {
                    whiteExtraSeconds += Integer.min(extraSeconds, whiteMoveUsed);
                } else {
                    blackExtraSeconds += Integer.min(extraSeconds, blackMoveUsed);
                }
                break;
            default:
                break;
        }
    }

}
